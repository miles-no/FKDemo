package no.fjordkraft.im.services;

import no.fjordkraft.im.model.InvoicePdf;

import java.util.List;

/**
 * Created by bhavi on 6/16/2017.
 */
public interface InvoiceService {

    InvoicePdf saveInvoicePdf(InvoicePdf invoicePdf);
    List<InvoicePdf> getInvoicePdfsByStatementId(Long statementId);
    void deleteInvoicePDFsByStatementId(Long statementId);
}
