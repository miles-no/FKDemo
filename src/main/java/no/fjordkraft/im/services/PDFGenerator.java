package no.fjordkraft.im.services;

import no.fjordkraft.im.model.Statement;

/**
 * Created by miles on 5/12/2017.
 */
public interface PDFGenerator {
    public void generateInvoicePDF() throws InterruptedException;
    public void generateInvoicePDFSingleStatement(Statement statement);
}
