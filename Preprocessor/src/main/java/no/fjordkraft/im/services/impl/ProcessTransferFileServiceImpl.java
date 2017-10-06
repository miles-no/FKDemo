package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.exceptions.StatementSplitterException;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.model.TransferFileArchive;
import no.fjordkraft.im.model.TransferFileId;
import no.fjordkraft.im.services.*;
import no.fjordkraft.im.statusEnum.SystemBatchInputStatusEnum;
import no.fjordkraft.im.task.SplitterTask;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by miles on 8/21/2017.
 */
@Service
public class ProcessTransferFileServiceImpl implements ProcessTransferFileService,ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(StatementServiceImpl.class);

    @Autowired
    SystemBatchInputService systemBatchInputService;

    ApplicationContext applicationContext;

    @Autowired
    @Qualifier("FileSplitterExecutor")
    TaskExecutor taskExecutor;

    @Autowired
    TransferFileArchiveService transferFileArchiveService;

    @Autowired
    StatementSplitter statementSplitter;

    @Autowired
    AuditLogService auditLogService;

    @Autowired
    TransferFileService transferFileService;

    @Transactional
    public void processTransferFile(SystemBatchInput systemBatchInput){
        logger.debug("Fetch and split file "+systemBatchInput.getTransferFile().getFilename());
        systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.NEW.getStatus());
        SplitterTask splitterTask = applicationContext.getBean(SplitterTask.class,systemBatchInput);
        taskExecutor.execute(splitterTask);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void splitAndSave(SystemBatchInput systemBatchInput){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("SplitStatementFile with systemBatchInputId " + systemBatchInput.getId() + " Filename " + systemBatchInput.getTransferFile().getFilename() );
        try {
            //systemBatchInput = systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSING.getStatus());
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

            systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSING.getStatus());
            /*auditLogService.saveAuditLog(IMConstants.SYSTEM_BATCH_FILE, systemBatchInput.getId(), SystemBatchInputStatusEnum.PROCESSING.getStatus(),
                    null, IMConstants.SUCCESS);*/
            stopWatch.stop();
            logger.debug("File split successful for file " + transferFileArchive.getFilename() + " with id " + systemBatchInput.getId()+ stopWatch.prettyPrint());
        }   catch (Exception e) {
            logger.error("Exception while splitting file " +systemBatchInput.getTransferFile().getFilename()+ " id " + systemBatchInput.getId(), e);
            systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.FAILED.getStatus());
            transferFileService.updateTransferFile(systemBatchInput.getTransferFile().getCompositeKey(), SystemBatchInputStatusEnum.FAILED.getStatus());
            auditLogService.saveAuditLog(IMConstants.SYSTEM_BATCH_FILE, systemBatchInput.getId(), SystemBatchInputStatusEnum.FAILED.getStatus(),
                    e.getMessage(), IMConstants.SUCCESS);
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
