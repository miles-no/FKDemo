package no.fjordkraft.im.preprocess.services.impl;

//import no.fjordkraft.im.util.SetInvoiceASOnline;
import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.preprocess.services.Preprocessor;
import no.fjordkraft.im.preprocess.services.PreprocessorEngine;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.PDFGenerator;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.services.impl.AuditLogServiceImpl;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.task.PreprocessorTask;
import no.fjordkraft.im.util.IMConstants;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by bhavi on 5/8/2017.
 */
@Service
public class PreprocessorServiceImpl implements PreprocessorService,ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(PreprocessorServiceImpl.class);

    @Autowired
    @Qualifier("unmarshaller")
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

    @Autowired
    private ConfigService configService;

    @Autowired
    AuditLogServiceImpl auditLogService;

    @Autowired
    PDFGenerator pdfGenerator;


    private Statement statement;

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
            Reader reader = new InputStreamReader(inputStream,StandardCharsets.ISO_8859_1);
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
        Long numOfThreads = configService.getLong(IMConstants.NUM_OF_THREAD_PREPROCESSOR);
        List<no.fjordkraft.im.model.Statement> statementList = statementRepository.readStatements(numOfThreads, StatementStatusEnum.PENDING.name());
        logger.debug("Preprocessing started for "+ statementList.size() + " statements");
        if(taskExecutor instanceof ThreadPoolTaskExecutor) {
            ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor)taskExecutor;
            logger.debug("Processor Thread queue count " + executor.getThreadPoolExecutor().getQueue().size() +" active threads "+ executor.getActiveCount() + "max pool size "+executor.getMaxPoolSize()+ " :: "+executor.getThreadPoolExecutor().getActiveCount());
        }
        for(no.fjordkraft.im.model.Statement statement:statementList) {
            //statement = statementService.updateStatement(statement, StatementStatusEnum.PRE_PROCESSING);
            statement.getSystemBatchInput().getTransferFile().getFilename();
            statement.getStatementPayload();

            logger.debug("Statement with id "+ statement.getId() + " invoice number "+ statement.getInvoiceNumber() +" sent for preprocessing ");
            PreprocessorTask preprocessorTask = applicationContext.getBean(PreprocessorTask.class,statementService,statement);
            taskExecutor.execute(preprocessorTask);
        }
        stopwatch.stop();
        logger.debug("Time for preprocessing "+statementList.size() + " " + stopwatch.prettyPrint());
    }

    @Transactional()
    public void preprocess(no.fjordkraft.im.model.Statement statement) {
        StopWatch stopwatch = new StopWatch("Preprocessing");
        stopwatch.start();
        Statement if320statement = null;
        try {
            logger.debug("Preprocessing statement with id " + statement.getId());
            String payload = statement.getStatementPayload().getPayload();
            statement.getSystemBatchInput().getTransferFile().getFilename();
            if320statement = unmarshallStatement(new ByteArrayInputStream(payload.getBytes(StandardCharsets.ISO_8859_1)));
            getUpdatedStatementEntity(if320statement, statement);

            PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request = new PreprocessRequest();
            request.setStatement(if320statement);
            request.setEntity(statement);
            if320statement.setOnline(statement.isOnline());

            //statement = statementService.updateStatement(statement, StatementStatusEnum.PRE_PROCESSING);
         /*   preprocessorEngine.execute(request);
            statement.setLayoutID(request.getEntity().getLayoutID());
            statement = statementService.updateStatement(statement, StatementStatusEnum.PRE_PROCESSED);*/
            //if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())
            logger.info(" is online statement id " + statement.getId() + " invoice number  "+ statement.getInvoiceNumber() + " online "+ statement.isOnline());
            if(!request.getStatement().isOnline()) {
                statement = statementService.updateStatement(statement, StatementStatusEnum.PRE_PROCESSING);
                logger.info(" is online statement id " + statement.getId() + " invoice number  "+ statement.getInvoiceNumber() + " online "+ statement.isOnline());
            }
            else
            {
                statement.getSystemBatchInput().setBrand(statement.getBrand());
                statement.setStatus(StatementStatusEnum.PRE_PROCESSING.getStatus());
                List<Preprocessor> preProcessorList = preprocessorEngine.getPreprocessorList();
                Map<Preprocessor,Boolean> preProcessorMap= new HashMap<Preprocessor,Boolean>();
                for(Preprocessor preprocessor :preProcessorList)
                {
                    PreprocessorInfo annotationObj1 = preprocessor.getClass().getAnnotation(PreprocessorInfo.class);
                    if(annotationObj1.skipOnline()) {
                       preProcessorMap.put(preprocessor,annotationObj1.skipOnline());
                    }
                }
                preprocessorEngine.setPreprocessorMap(preProcessorMap);
            }
            preprocessorEngine.execute(request);
            statement.setLayoutID(request.getEntity().getLayoutID());
            statement.setNoOfMeter(request.getEntity().getNoOfMeter());
            statement.setEhfAttachment(request.getEntity().isEhfAttachment());
            statement.setE2bAttachment(request.getEntity().isE2bAttachment());

            //if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())
            logger.info("Updating status of statement with id " + statement.getId() + " invoice number " + statement.getInvoiceNumber() + " is online " + statement.isOnline() +" to preprocessed ");
            if (!request.getStatement().isOnline()) {
                //logger.info("Updating status of statement with id " + statement.getId() + " invoice number " + statement.getInvoiceNumber() + " to preprocessed ");
                statement = statementService.updateStatement(statement, StatementStatusEnum.PRE_PROCESSED);
            }
            else
            {
                statement.setStatus(StatementStatusEnum.PRE_PROCESSED.getStatus());
                setStatement(request.getStatement());
            }
            //auditLogService.saveAuditLog(statement.getId(), StatementStatusEnum.PRE_PROCESSED.getStatus(), null, IMConstants.SUCCESS);
            stopwatch.stop();
            logger.debug("Preprocessing completed for statement with id "+ statement.getId());
            logger.debug(stopwatch.prettyPrint());
        } catch (PreprocessorException ex) {
           //if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())    {
            if(!if320statement.isOnline()) {
                //SetInvoiceASOnline.unset();
                logger.error("Exception in preprocessor task for statement with id " + statement.getId().toString(), ex);
                statement = statementService.updateStatement(statement, StatementStatusEnum.PRE_PROCESSING_FAILED);
                auditLogService.saveAuditLog(statement.getId(), StatementStatusEnum.PRE_PROCESSING.getStatus(), ex.getMessage(), IMConstants.ERROR,statement.getLegalPartClass());
           }
            else
           {
               //SetInvoiceASOnline.unset();
               logger.error("Exception in preprocessor task for Online file " + statement.getFileName(), ex);
               statement.setStatus( StatementStatusEnum.PRE_PROCESSING_FAILED.getStatus());
               throw ex;
           }
        } catch (Exception e) {
            //if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())   {
            if(!if320statement.isOnline()) {
                logger.error("Exception in preprocessor task for statement with id " + statement.getId().toString(), e);
                statementService.updateStatement(statement, StatementStatusEnum.PRE_PROCESSING_FAILED);
            }
            else
            {
                logger.error("Exception in preprocessor task for statement for Online file " + statement.getFileName(),e);
                statement.setStatus(StatementStatusEnum.PRE_PROCESSING_FAILED.getStatus());

            }
        }

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
        statementEntity.setLegalPartClass(statement.getLegalPartClass());
        statementEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        statementEntity.setCreditLimit(statement.getCreditLimit());
        logger.debug("updating statement  "+ statementEntity.getId() + " statementOcr " + statementOcr + " customerId " + customerId + " accountNumber "+ accountNumber + " invoiceNumber "+  invoiceNumber );
        return statementEntity;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setUnMarshaller(Unmarshaller unMarshaller) {
        this.unMarshaller = unMarshaller;
    }

    public Statement getStatement() {
        return statement;
    }


    public void setStatement(Statement statement) {
        this.statement = statement;
    }
}
