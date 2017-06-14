package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.services.PreprocessorEngine;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.task.PreprocessorTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.oxm.Unmarshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by bhavi on 5/8/2017.
 */
@Service
public class PreprocessorServiceImpl implements PreprocessorService,ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(PreprocessorServiceImpl.class);

    @Autowired
    Unmarshaller unMarshaller;

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    PreprocessorEngine preprocessorEngine;

    @Autowired
    @Qualifier("PreprocessorExecutor")
    TaskExecutor taskExecutor;

    @Autowired
    StatementService statementService;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public Statement unmarshallStatement(String path) throws IOException {
        try {
            InputStream inputStream = new FileInputStream(path);
            return unmarshallStatement(inputStream);
        } catch(Exception e){
            logger.error("Exception while unmarshalling statement " + path, e);
            throw e;
        }
    }

    @Override
    public Statement unmarshallStatement(InputStream inputStream) throws IOException {
        try {
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1);
            StreamSource source = new StreamSource(reader);
            Statement stmt = (Statement)unMarshaller.unmarshal(source);
            return stmt;
        } catch(Exception e){
            logger.error("Exception while unmarshalling statement ",e);
            throw e;
        }
    }

    @Transactional
    public void preprocess() throws IOException {
        StopWatch stopwatch = new StopWatch("Preprocessing");
        stopwatch.start();
        List<no.fjordkraft.im.model.Statement> statementList = statementRepository.readStatements(StatementStatusEnum.PENDING.name());
        logger.debug("Preprocessing started for "+ statementList.size() + " statements");
        if(taskExecutor instanceof ThreadPoolTaskExecutor) {
            ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor)taskExecutor;
            logger.debug("Processor Thread queue count " + executor.getThreadPoolExecutor().getQueue().size() +" active threads "+ executor.getActiveCount() + "max pool size "+executor.getMaxPoolSize()+ " :: "+executor.getThreadPoolExecutor().getActiveCount());
        }
        for(no.fjordkraft.im.model.Statement statement:statementList) {
            statement.getSystemBatchInput().getFilename();
            statement.getStatementPayload();
            statementService.updateStatement(statement,StatementStatusEnum.PRE_PROCESSING);
            PreprocessorTask preprocessorTask = applicationContext.getBean(PreprocessorTask.class,statement);
            taskExecutor.execute(preprocessorTask);
        }
        stopwatch.stop();
        logger.debug("Time for preprocessing "+statementList.size() + " " + stopwatch.prettyPrint());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void preprocess(no.fjordkraft.im.model.Statement statement) {
        StopWatch stopwatch = new StopWatch("Preprocessing");
        stopwatch.start();
        try {
            logger.debug("Preprocessing statement with id " + statement.getId());
            String payload = statement.getStatementPayload().getPayload();
            statement.getSystemBatchInput().getFilename();
            Statement if320statement = unmarshallStatement(new ByteArrayInputStream(payload.getBytes(StandardCharsets.ISO_8859_1)));
            statementService.updateStatement(getUpdatedStatementEntity(if320statement, statement));

            PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request = new PreprocessRequest();
            request.setStatement(if320statement);
            request.setEntity(statement);

            preprocessorEngine.execute(request);
            statementService.updateStatement(statement, StatementStatusEnum.PRE_PROCESSED);
        } catch (Exception e) {
            logger.error("Exception in preprocessor task for statement with id "+statement.getId().toString(), e);
            statementService.updateStatement(statement,StatementStatusEnum.PRE_PROCESSING_FAILED);
        }
        stopwatch.stop();
        logger.debug("Preprocessing completed for statement with id "+ statement.getId());
        logger.debug(stopwatch.prettyPrint());
    }


    public no.fjordkraft.im.model.Statement getUpdatedStatementEntity(Statement statement,no.fjordkraft.im.model.Statement statementEntity) {
        Long statementOcr = statement.getStatementOcrNumber();
        Integer customerId = statement.getNationalId();
        Long accountNumber = statement.getAccountNumber();
        String invoiceNumber = accountNumber + ""+ statement.getSequenceNumber();
        Date invoiceDate = statement.getStatementDate().toGregorianCalendar().getTime();
        Date dueDate = statement.getDueDate().toGregorianCalendar().getTime();
        statementEntity.setStatementId(statementOcr.toString());
        statementEntity.setCustomerId(customerId.toString());
        statementEntity.setAccountNumber(accountNumber.toString());
        statementEntity.setInvoiceNumber(invoiceNumber);
        statementEntity.setCity(statement.getCity());
        statementEntity.setVersion(statement.getVersion());
        statementEntity.setDistributionMethod(statement.getDistributionMethod());
        statementEntity.setAmount(statement.getCurrentClaim());
        statementEntity.setInvoiceDate(invoiceDate);
        statementEntity.setDueDate(dueDate);
        statementEntity.setUdateTime(new Timestamp(System.currentTimeMillis()));
        logger.debug("updating statement  "+ statementEntity.getId() + " statementOcr " + statementOcr + " customerId " + customerId + " accountNumber "+ accountNumber + " invoiceNumber "+  invoiceNumber );
        return statementEntity;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
