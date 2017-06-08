package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.domain.RestInvoicePdf;
import no.fjordkraft.im.domain.RestStatement;
import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.model.StatementPayload;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.repository.StatementDetailRepository;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.repository.SystemBatchInputRepository;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.services.StatementSplitter;
import no.fjordkraft.im.services.SystemBatchInputService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.statusEnum.SystemBatchInputStatusEnum;
import no.fjordkraft.im.task.SplitterTask;
import org.apache.commons.io.FileUtils;
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

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    public void splitSystemBatchInputFile() {
        StopWatch stopWatch = new StopWatch(getClass().getSimpleName());
        stopWatch.start("Statement Splitter");
        SystemBatchInput systemBatchInput = null;
        try {
            InputStream inputStream = null;
            systemBatchInput = systemBatchInputRepository.readSingleSystemBatchInputFile(String.valueOf(SystemBatchInputStatusEnum.PENDING.getStatus()));
            if(null != systemBatchInput) {
                String payload = systemBatchInput.getSystemBatchInputPayload().getPayload();
                inputStream = new ByteArrayInputStream(payload.getBytes(StandardCharsets.ISO_8859_1));
                systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSING.getStatus());
                statementSplitter.batchFileSplit(inputStream, systemBatchInput.getFilename(), systemBatchInput);
                systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSED.getStatus());
                logger.debug("File split successful for file " + systemBatchInput.getFilename() + " with id " + systemBatchInput.getId());
            }
            /*List<SystemBatchInput> systemBatchInputList = systemBatchInputRepository.readSystemBatchInputFile(String.valueOf(SystemBatchInputStatusEnum.PENDING.getStatus()));
            if(null != systemBatchInputList &&  systemBatchInputList.size()>0) {
                for(SystemBatchInput systemBatchInput:systemBatchInputList){
                    SplitterTask splitterTask = new SplitterTask(systemBatchInput);
                    taskExecutor.execute(splitterTask);
                }

            }*/

        } catch(Exception ex) {
            //if(transferFileID > 0) {
            systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.FAILED.getStatus());
            //}
            throw new RuntimeException(ex.getMessage());
        }
        stopWatch.stop();
        logger.debug(stopWatch.prettyPrint());
    }


    public void fetchAndSplit(){
        List<SystemBatchInput> systemBatchInputList = systemBatchInputRepository.readSystemBatchInputFile(String.valueOf(SystemBatchInputStatusEnum.PENDING.getStatus()));
        if(null != systemBatchInputList &&  systemBatchInputList.size()>0) {
            logger.debug("Fetch and split "+systemBatchInputList.size()+ " files");
            for(SystemBatchInput systemBatchInput:systemBatchInputList){
                systemBatchInput.getSystemBatchInputPayload();
                systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSING.getStatus());
                SplitterTask splitterTask = applicationContext.getBean(SplitterTask.class,systemBatchInput);
                taskExecutor.execute(splitterTask);
            }

        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void splitAndSave(SystemBatchInput systemBatchInput){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("SplitStatementFile with systemBatchInputId " + systemBatchInput.getId() + " Filename " + systemBatchInput.getFilename() );
        try {
            String payload = systemBatchInput.getSystemBatchInputPayload().getPayload();
            InputStream inputStream = new ByteArrayInputStream(payload.getBytes(StandardCharsets.ISO_8859_1));
            statementSplitter.batchFileSplit(inputStream, systemBatchInput.getFilename(), systemBatchInput);
            systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.PROCESSED.getStatus());
            logger.debug("File split successful for file " + systemBatchInput.getFilename() + " with id " + systemBatchInput.getId()+ stopWatch.prettyPrint());
        } catch (Exception e) {
            logger.error("Exception while splitting file "+ systemBatchInput.getFilename()+ " id "+ systemBatchInput.getId(),e);
            systemBatchInputService.updateStatusOfIMSystemBatchInput(systemBatchInput, SystemBatchInputStatusEnum.FAILED.getStatus());
        }
        stopWatch.stop();
        logger.debug("Time for splitting file with id "+ systemBatchInput.getId() + " Filename "+ systemBatchInput.getFilename() + stopWatch.prettyPrint());
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStatement(Statement statement){
        statementRepository.saveAndFlush(statement);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStatement(Statement statement, StatementStatusEnum status){
        statement.setStatus(status.getStatus());
        statement.setUdateTime(new Timestamp(System.currentTimeMillis()));
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
        imStatement.setUdateTime(new Timestamp(System.currentTimeMillis()));

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
        imStatement.setUdateTime(new Timestamp(System.currentTimeMillis()));

        StatementPayload statementPayload = new StatementPayload();
        statementPayload.setPayload(xml);
        statementPayload.setStatement(imStatement);

        imStatement.setStatementPayload(statementPayload);
        statementRepository.saveAndFlush(imStatement);
    }

    @Override
    public List<RestStatement> getDetails(int page, int size, String status, Timestamp fromTime, Timestamp toTime) {
        List<Statement> statementList = statementDetailRepository.getDetails(page, size, status, fromTime, toTime);

        List<RestStatement> restStatementList = new ArrayList<>();
        //List<Long> statementIdList =  new ArrayList<>();
        Map<Long,RestStatement> statementMap = new HashMap<Long,RestStatement>();
        if(null != statementList) {
            for (Statement statement : statementList) {
                RestStatement restStatement = new RestStatement();
                restStatement.setId(statement.getId());
                restStatement.setStatus(statement.getStatus());
                restStatement.setUdateTime(statement.getUdateTime());
                restStatement.setPdfAttachment(statement.getPdfAttachment());
                restStatement.setAccountNumber(statement.getAccountNumber());
                restStatement.setAmount(statement.getAmount());
                restStatement.setInvoiceNumber(statement.getInvoiceNumber());
                restStatement.setCustomerId(statement.getCustomerId());
                restStatement.setDistributionMethod(statement.getDistributionMethod());
                restStatement.setVersion(statement.getVersion());
                restStatement.setCity(statement.getCity());
                restStatement.setStatementId(statement.getStatementId());
                restStatement.setDueDate(statement.getDueDate());
                restStatement.setCreateTime(statement.getCreateTime());
                restStatementList.add(restStatement);
                //statementIdList.add(statement.getId());
                statementMap.put(statement.getId(),restStatement);
            }
        }

        List<RestInvoicePdf> invoicePdfList = statementDetailRepository.getInvoicePdfs(statementMap.keySet());

        for(RestInvoicePdf restInvoicePdf : invoicePdfList) {
            RestStatement restStatement = statementMap.get(restInvoicePdf.getStatementId());
            restStatement.getInvoicePdfList().add(restInvoicePdf);
        }
        return restStatementList;
    }
}
