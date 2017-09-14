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
import java.util.List;

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


    @Override
    @Transactional
    public void generateInvoicePDF() throws InterruptedException {
        Long numOfThreads = configService.getLong(IMConstants.NUM_OF_THREAD_PDFGENERATOR);

        List<Statement> statements = statementRepository.readStatements(numOfThreads, StatementStatusEnum.PRE_PROCESSED.getStatus());
        logger.debug("Generate invoice pdf for "+ statements.size() + " statements");

        List<Long> statementList = new ArrayList<>(50);
        for(Statement statement:statements) {
            logger.debug("Statement with id "+ statement.getId()+ " updated to SENT_FOR_PDF_PROCESSING ");
            statementService.updateStatement(statement, StatementStatusEnum.SENT_FOR_PDF_PROCESSING);
            //pdfGeneratorClient.processStatement(statement.getId());
            statementList.add(statement.getId());
            if(statementList.size() == 50) {
                pdfGeneratorClient.processStatement(statementList);
                statementList.clear();
            }
        }

        if(statementList.size()>0){
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
