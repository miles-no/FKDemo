package no.fjordkraft.im.services;

import com.itextpdf.text.DocumentException;
import no.fjordkraft.im.model.Statement;

import java.io.IOException;

/**
 * Created by miles on 6/6/2017.
 */
public interface InvoiceGenerator {

    void generateInvoice(Statement statement, byte[] generatedPdf) throws IOException, DocumentException;

    //void generateInvoice(Statement statement) throws IOException, DocumentException;
}
