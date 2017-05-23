package no.fjordkraft.im.task;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.services.PDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by bhavi on 5/22/2017.
 */

@Service
@Scope("prototype")
public class PDFGeneratorTask implements Runnable {

    Statement statement;

    @Autowired
    PDFGenerator pdfGenerator;

    public PDFGeneratorTask(Statement statement){
        this.statement = statement;
    }

    @Override
    public void run() {
        pdfGenerator.generateInvoicePDFSingleStatement(statement);
    }
}
