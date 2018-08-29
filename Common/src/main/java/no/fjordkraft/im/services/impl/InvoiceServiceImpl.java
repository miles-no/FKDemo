package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.repository.InvoicePdfRepository;
import no.fjordkraft.im.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * Created by bhavi on 6/16/2017.
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoicePdfRepository invoicePdfRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public InvoicePdf saveInvoicePdf(InvoicePdf invoicePdf) {
        invoicePdf = invoicePdfRepository.saveAndFlush(invoicePdf);
        return invoicePdf;
    }

    @Override
    public void deleteInvoicePDFsByStatementId(Long statementId) {
        List<InvoicePdf> invoicePdfList = getInvoicePdfsByStatementId(statementId);
        for(InvoicePdf invoicePdf:invoicePdfList) {
            invoicePdfRepository.delete(invoicePdf.getId());
        }
    }

    @Override
    public int deleteInvoicePDFsByDate(java.util.Date tillDate) {
        //To change body of implemented methods use File | Settings | File Templates.
       return invoicePdfRepository.deleteInvoicePDFsTillDate(tillDate);
    }

    @Override
    public List<InvoicePdf> getInvoicePdfsByStatementId(Long statementId) {
        return invoicePdfRepository.getInvoicePDFsByStatementId(statementId);
    }

}
