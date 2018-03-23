package no.fjordkraft.im.services;

import no.fjordkraft.im.model.Statement;

import java.io.InputStream;
import java.util.List;

/**
 * Created by miles on 5/12/2017.
 */
public interface PDFGenerator {
    public void generateInvoicePDF() throws InterruptedException;
    public void generateInvoicePDF(List<Long> statementIdList);
    public List<Long> getStatementIDsForPDFGen() throws InterruptedException;
    public byte[] generateInvoiceForSingleStatement(String processFilePath,String brand,Long layoutIdo);
}
