package no.fjordkraft.im.services;

import no.fjordkraft.im.model.Statement;

/**
 * Created by miles on 6/6/2017.
 */
public interface InvoiceGenerator {

    void generateInvoice(Statement statement);
}
