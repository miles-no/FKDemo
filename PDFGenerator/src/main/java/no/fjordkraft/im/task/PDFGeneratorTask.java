package no.fjordkraft.im.task;

import no.fjordkraft.im.model.Statement;
//import no.fjordkraft.im.services.PDFGenerator;
import no.fjordkraft.im.services.PDFGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Created by bhavi on 5/22/2017.
 */

@Service
@Scope("prototype")
public class PDFGeneratorTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PDFGeneratorTask.class);
    Statement statement;

    @Autowired
    PDFGenerator pdfGenerator;

    Long statementId;

    public PDFGeneratorTask(Statement statement){
        this.statement = statement;
    }

    public PDFGeneratorTask(Long statementId){
        this.statementId = statementId;
    }

    @Override
    //@Transactional
    public void run() {
        logger.debug(" transaction name ::"+ TransactionSynchronizationManager.getCurrentTransactionName());
       pdfGenerator.generateInvoicePDFSingleStatement(statement);
    }
}
