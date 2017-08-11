package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.domain.RestInvoicePdf;
import no.fjordkraft.im.domain.RestStatement;
import no.fjordkraft.im.exceptions.StatementSplitterException;
import no.fjordkraft.im.model.*;
import no.fjordkraft.im.repository.StatementDetailRepository;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.repository.SystemBatchInputRepository;
import no.fjordkraft.im.services.*;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.statusEnum.SystemBatchInputStatusEnum;
import no.fjordkraft.im.statusEnum.UIStatementStatusEnum;
import no.fjordkraft.im.task.SplitterTask;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bhavi on 5/9/2017.
 */
@Service
public class StatementServiceImpl implements StatementService,ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(StatementServiceImpl.class);

    @Autowired
    SystemBatchInputRepository systemBatchInputRepository;

    @Autowired
    StatementSplitter statementSplitter;

    @Autowired
    @Qualifier("FileSplitterExecutor")
    TaskExecutor taskExecutor;

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    SystemBatchInputService systemBatchInputService;

    @Resource
    StatementDetailRepository statementDetailRepository;

    ApplicationContext applicationContext;

    @Autowired
    TransferFileArchiveService transferFileArchiveService;

    @Autowired
    TransferFileServiceImpl transferFileService;

    @Autowired
    AuditLogService auditLogService;

    @Value("${if320.skip.bytes}")
    Boolean skipBytes;


    @Transactional
    public void processTransferFile(SystemBatchInput systemBatchInput){
            logger.debug("Fetch and split file "+systemBatchInput.getTransferFile().getFilename());
            //systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSING.getStatus());
            SplitterTask splitterTask = applicationContext.getBean(SplitterTask.class,systemBatchInput);
            taskExecutor.execute(splitterTask);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void splitAndSave(SystemBatchInput systemBatchInput){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("SplitStatementFile with systemBatchInputId " + systemBatchInput.getId() + " Filename " + systemBatchInput.getTransferFile().getFilename() );
        try {
            systemBatchInput = systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSING.getStatus());
            TransferFileId transferFileId = systemBatchInput.getTransferFile().getCompositeKey();
            TransferFileArchive transferFileArchive = transferFileArchiveService.findOne(transferFileId);
            String payload = transferFileArchive.getFileContent();
            InputStream inputStream = new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8));
            PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
            byte[] b = new byte[1];
            pushbackInputStream.read(b);
            logger.debug("byte read:: "+b[0]);
            if (b[0] == -62) {
                pushbackInputStream.read(b);
                logger.debug("byte 2:: "+b[0]);
            } else {
                logger.debug("unread byte");
                pushbackInputStream.unread(b);
            }

            statementSplitter.batchFileSplit(pushbackInputStream, transferFileArchive.getFilename(), systemBatchInput);

            systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSED.getStatus());
            auditLogService.saveAuditLog(IMConstants.SYSTEM_BATCH_FILE, systemBatchInput.getId(), SystemBatchInputStatusEnum.PROCESSED.getStatus(),
                    null, IMConstants.SUCCESS);
            logger.debug("File split successful for file " + transferFileArchive.getFilename() + " with id " + systemBatchInput.getId()+ stopWatch.prettyPrint());
        } catch (StatementSplitterException ex) {
            auditLogService.saveAuditLog(IMConstants.SYSTEM_BATCH_FILE, systemBatchInput.getId(), SystemBatchInputStatusEnum.FAILED.getStatus(),
                    ex.getMessage(), IMConstants.ERROR);
        }   catch (Exception e) {
            logger.error("Exception while splitting file " +systemBatchInput.getTransferFile().getFilename()+ " id " + systemBatchInput.getId(), e);
            systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.FAILED.getStatus());
        }
        stopWatch.stop();
        logger.debug("Time for splitting file with id "+ systemBatchInput.getId() + " Filename "+ systemBatchInput.getTransferFile().getFilename() + stopWatch.prettyPrint());
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStatement(Statement statement){
        statementRepository.saveAndFlush(statement);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStatement(Statement statement, StatementStatusEnum status){
        statement.setStatus(status.getStatus());
        statement.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        statementRepository.saveAndFlush(statement);
    }


    public List<Statement> getStatementsByStatus(StatementStatusEnum statementStatusEnum){
        return statementRepository.readStatements(statementStatusEnum.name());
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Transactional()
    public void saveIMStatementinDB(File statementFile, Statement imStatement) throws IOException {
        String xml = FileUtils.readFileToString(statementFile, StandardCharsets.ISO_8859_1);

        //imStatement.setPayload(xml);
        imStatement.setStatus(StatementStatusEnum.PENDING.getStatus());
        imStatement.setCreateTime(new Timestamp(System.currentTimeMillis()));
        imStatement.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        StatementPayload statementPayload = new StatementPayload();
        statementPayload.setPayload(xml);
        statementPayload.setStatement(imStatement);
        statementRepository.saveAndFlush(imStatement);
    }

    @Transactional()
    public void saveIMStatementinDB(String xml, Statement imStatement) throws IOException {
       // String xml = FileUtils.readFileToString(statementFile, StandardCharsets.ISO_8859_1);

        //imStatement.setPayload(xml);
        imStatement.setStatus(StatementStatusEnum.PENDING.getStatus());
        imStatement.setCreateTime(new Timestamp(System.currentTimeMillis()));
        imStatement.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        StatementPayload statementPayload = new StatementPayload();
        statementPayload.setPayload(xml);
        statementPayload.setStatement(imStatement);

        imStatement.setStatementPayload(statementPayload);
        statementRepository.saveAndFlush(imStatement);
    }

}
