package no.fjordkraft.im.services;

import no.fjordkraft.im.model.Statement;
import org.eclipse.birt.core.exception.BirtException;

import java.io.IOException;

/**
 * Created by miles on 5/12/2017.
 */
public interface PDFGenerator {
    public void generateInvoicePDF() throws InterruptedException;
   // public void generateInvoicePDFSingleStatement(Statement statement);
    public byte[] generatePreview(Long layoutId, Integer version) throws IOException, BirtException;
    public void processStatement(Statement statement);
    public void processStatement(Long statementId);
    public void generateInvoicePDFSingleStatement(Statement statement);

}
