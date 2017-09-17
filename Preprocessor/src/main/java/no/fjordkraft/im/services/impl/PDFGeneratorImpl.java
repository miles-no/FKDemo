package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.intercomm.PDFGeneratorClient;
import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.PDFGenerator;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            logger.debug("Statement with id "+ statement.getId()+ " updated to SENT_FOR_PDF_PROCESSING ");
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

        /*for(Statement statement:statements) {
            logger.debug("Statement with id "+ statement.getId()+ " sent for PDF generation");
            //statementService.updateStatement(statement, StatementStatusEnum.SENT_FOR_PDF_PROCESSING);
            pdfGeneratorClient.processStatement(statement.getId());
        }*/
    }

    public void generateInvoicePDF(Statement statement) {
        //statementService.updateStatement(statement, StatementStatusEnum.SENT_FOR_PDF_PROCESSING);
        pdfGeneratorClient.processStatement(statement.getId());
    }

}
