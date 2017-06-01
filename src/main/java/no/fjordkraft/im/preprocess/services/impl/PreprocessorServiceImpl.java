package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Attachment;
import no.fjordkraft.im.if320.models.FAKTURA;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
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

    ApplicationContext applicationContext;

    @Override
    public Statement unmarshallStatement(String path) throws IOException {
        try {
            InputStream inputStream = new FileInputStream(path);
            return unmarshallStatement(inputStream);
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Statement unmarshallStatement(InputStream inputStream) throws IOException {
        try {
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1);
            StreamSource source = new StreamSource(reader);
            System.out.println(unMarshaller.supports(Statement.class));
            Statement stmt = (Statement)unMarshaller.unmarshal(source);
            System.out.println(stmt);
            return stmt;
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        }

    }


    @Transactional
    public void preprocess() throws IOException {
        StopWatch stopwatch = new StopWatch("Preprocessing");
        stopwatch.start();
        List<no.fjordkraft.im.model.Statement> statementList = statementRepository.readStatements(StatementStatusEnum.PENDING.name());
        logger.debug("Preprocessing "+ statementList.size() + " statements");
        for(no.fjordkraft.im.model.Statement statement:statementList) {
            statement.getSystemBatchInput().getFilename();
            statement.setStatus(StatementStatusEnum.PRE_PROCESSING.getStatus());
            updateStatementEntity(statement);
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
            //statement.getSystemBatchInput().getFilename();
            logger.debug("Preprocessing statement with id " + statement.getId());
            String payload = statement.getPayload();
            statement.getSystemBatchInput().getFilename();
            Statement if320statement = unmarshallStatement(new ByteArrayInputStream(payload.getBytes(StandardCharsets.ISO_8859_1)));
            updateStatementEntity(getUpdatedStatementEntity(if320statement, statement));
            PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request = new PreprocessRequest();
            request.setStatement(if320statement);
            request.setEntity(statement);
            preprocessorEngine.execute(request);
            statement.setStatus(StatementStatusEnum.PRE_PROCESSED.getStatus());
            statement.setUdateTime(new Timestamp(System.currentTimeMillis()));
            updateStatementEntity(statement);
        } catch (Exception e) {
            logger.error("Exception in preprocessor task for statement with id "+statement.getId().toString(), e);
            statement.setStatus(StatementStatusEnum.PRE_PROCESSING_FAILED.getStatus());
            statement.setUdateTime(new Timestamp(System.currentTimeMillis()));
            statementService.updateStatement(statement);
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
        statementEntity.setStatementId(statementOcr.toString());
        statementEntity.setCustomerId(customerId.toString());
        statementEntity.setAccountNumber(accountNumber.toString());
        statementEntity.setInvoiceNumber(invoiceNumber);
        statementEntity.setCity(statement.getCity());
        statementEntity.setVersion(statement.getVersion());
        statementEntity.setDistributionMethod(statement.getDistributionMethod());
        statementEntity.setUdateTime(new Timestamp(System.currentTimeMillis()));
        logger.debug("updating statement  "+ statementEntity.getId() + " statementOcr " + statementOcr + " customerId " + customerId + " accountNumber "+ accountNumber + " invoiceNumber "+  invoiceNumber );
        return statementEntity;
    }

    private void updateStatementEntity(no.fjordkraft.im.model.Statement statementEntity){
        statementService.updateStatement(statementEntity);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
