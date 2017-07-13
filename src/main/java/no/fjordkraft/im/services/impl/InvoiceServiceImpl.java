package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.repository.InvoicePdfRepository;
import no.fjordkraft.im.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
}
