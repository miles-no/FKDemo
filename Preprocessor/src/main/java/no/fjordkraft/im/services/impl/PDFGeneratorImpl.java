package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.intercomm.PDFGeneratorClient;
import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.model.StatementsList;
import no.fjordkraft.im.publisher.InvoiceGeneratorPublisher;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.PDFGenerator;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by miles on 5/12/2017.
 */
@Service
public class PDFGeneratorImpl implements PDFGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PDFGeneratorImpl.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    StatementService statementService;

    @Autowired
    AuditLogServiceImpl auditLogService;

    @Autowired
    PDFGeneratorClient pdfGeneratorClient;

    private static Set<Long> statementIdSet = new HashSet<>();

   /* @Autowired
    InvoiceGeneratorPublisher invoiceGeneratorPublisher;*/


    @Override
    @Transactional
    public void generateInvoicePDF() throws InterruptedException {
        Long numOfThreads = configService.getLong(IMConstants.NUM_OF_STMT_PDF_GEN);

        List<Statement> statements = statementRepository.readStatements(numOfThreads, StatementStatusEnum.PRE_PROCESSED.getStatus());
        logger.debug("Generate invoice pdf for "+ statements.size() + " statements");

        List<Long> statementList = new ArrayList<>(50);
        for(Statement statement:statements) {
            if(statementIdSet.contains(statement.getId())){
                logger.debug("Statement with id "+ statement.getId() +  " already sent for pdf generation");
            }
            //logger.debug("Statement with id "+ statement.getId()+ " updated to SENT_FOR_PDF_PROCESSING ");
            try {
                statementService.updateStatement(statement, StatementStatusEnum.SENT_FOR_PDF_PROCESSING);
            }catch (Exception e ){
                logger.error("error comminting transaction in sendpdf",e);
            }

            //pdfGeneratorClient.processStatement(statement.getId());
            statementList.add(statement.getId());
            if(statementList.size() == 50) {
                statementIdSet.addAll(statementList);
                pdfGeneratorClient.processStatement(statementList);
                statementList.clear();
            }
        }

        if(statementList.size()>0){
            statementIdSet.addAll(statementList);
            pdfGeneratorClient.processStatement(statementList);
            statementList.clear();
        }
    }

    @Override
    @Transactional
    public List<Long> getStatementIDsForPDFGen() throws InterruptedException {
        Long numOfThreads = configService.getLong(IMConstants.NUM_OF_STMT_PDF_GEN);

        List<Statement> statements = statementRepository.readStatements(numOfThreads, StatementStatusEnum.PRE_PROCESSED.getStatus());
        logger.debug("Generate invoice pdf for "+ statements.size() + " statements");

        List<Long> statementList = new ArrayList<Long>(numOfThreads.intValue());
        for(Statement statement:statements) {
            if(statementIdSet.contains(statement.getId())){
                //logger.debug("Statement with id "+ statement.getId() +  " already sent for pdf generation");
            }
            //logger.debug("Statement with id "+ statement.getId()+ " updated to SENT_FOR_PDF_PROCESSING ");
            try {
                statementService.updateStatement(statement, StatementStatusEnum.PDF_PROCESSING);
                statementIdSet.add(statement.getId());
            }catch (Exception e ){
                logger.error("error commiting transaction in sendpdf",e);
            }
            statementList.add(statement.getId());
        }

        return statementList;
    }

    @Override
    public byte[] generateInvoiceForSingleStatement(String processFilePath,String brand,Long layoutId)     {
        String newResp = processFilePath+ "|brand:"+brand + "|layout:"+layoutId;
        logger.debug("Calling PDF generator client for Processed File " + processFilePath);
       return pdfGeneratorClient.processSingleStatement(processFilePath,brand,layoutId.toString());
    }


    public void generateInvoicePDF(List<Long> statementIdList) {
        Long numOfThreads = configService.getLong(IMConstants.NUM_OF_STMT_PDF_GEN);
        List<List<Long>> lists = ListUtils.partition(statementIdList,150);
        Boolean useKafkaForPDFProcessing = configService.getBoolean(IMConstants.USE_KAFKA_PDF_PROCESSING);
        for(List<Long> list : lists) {
            try {
                if (useKafkaForPDFProcessing) {
                    StatementsList statementsList = new StatementsList(list);
                    logger.debug("Sending statement to kafka size "+ list.size());
                    //invoiceGeneratorPublisher.publish(statementsList);
                } else {
                    pdfGeneratorClient.processStatement(list);
                }
            } catch (Exception e) {
                logger.error("Error in sending request to PDF Generator", e);
                logger.error("Error in sending request to PDF Generator message", e.getMessage());
                if(e.getMessage().contains("Read timed out")) {
                    logger.debug("read time out in feign client");
                } else {
                    /*for (Long statementId : list) {
                        Statement stmt = statementService.getStatement(statementId);
                        statementService.updateStatement(stmt, StatementStatusEnum.PRE_PROCESSED);
                    }*/
                }
            }
        }
    }

}
