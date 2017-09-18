package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.util.IMConstants;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.Invoice;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 9/8/2017.
 */
@Service
@PreprocessorInfo(order=11)
public class AttachmentPreprocessor extends BasePreprocessor {

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        Attachments attachments = request.getStatement().getAttachments();
        List<Attachment> attachmentList = new ArrayList<Attachment>();
        Nettleie nettleie;
        List<InvoiceLine120> invoiceLine120List = new ArrayList<>();
        BaseItemDetails baseItemDetails;
        List<BaseItemDetails> baseItemDetailsList = new ArrayList<>();
        InvoiceSummary invoiceSummary;
        InvoiceTotals invoiceTotals;

        for(Attachment attachment:attachments.getAttachment()) {
            if(IMConstants.EMUXML.equals(attachment.getFAKTURA().getVEDLEGGFORMAT())) {

                for(Attachment pdfAttachment:attachments.getAttachment()) {
                    if(attachment.getFAKTURA().getMAALEPUNKT() == pdfAttachment.getFAKTURA().getMAALEPUNKT()) {
                        if(IMConstants.PDFEHF.equals(pdfAttachment.getFAKTURA().getVEDLEGGFORMAT())) {
                            nettleie = new Nettleie();
                            baseItemDetails = new BaseItemDetails();
                            invoiceSummary = new InvoiceSummary();
                            invoiceTotals = new InvoiceTotals();
                            invoiceSummary.setInvoiceTotals(invoiceTotals);

                            Invoice invoice = pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice();

                            nettleie.setReferenceNumber(invoice.getID().getValue().toString());

                            invoiceSummary.getInvoiceTotals().setGrossAmount(Float.valueOf(pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice()
                                    .getLegalMonetaryTotal().getTaxInclusiveAmount().getValue().toString()));

                            invoiceSummary.getInvoiceTotals().setVatTotalsAmount(Float.valueOf(pdfAttachment.getFAKTURA().getVedleggehfObj()
                                    .getInvoice().getTaxTotals().get(0).getTaxSubtotals().get(0).getTaxAmount().getValue().toString()));

                            nettleie.setInvoiceSummary(invoiceSummary);

                            List<InvoiceLineType> invoiceLineTypeList = invoice.getInvoiceLines();
                            for(InvoiceLineType invoiceLineType:invoiceLineTypeList) {
                                baseItemDetails = new BaseItemDetails();
                                baseItemDetails.setDescription(invoiceLineType.getItem().getName().getValue().toString());
                                baseItemDetails.setQuantityInvoiced(Float.valueOf(invoiceLineType.getInvoicedQuantity().getValue().toString()));
                                baseItemDetails.setUnitOfMeasure(invoiceLineType.getInvoicedQuantity().getUnitCode());
                                baseItemDetails.setUnitPrice(Float.valueOf(invoiceLineType.getPrice().getPriceAmount().getValue().toString()));
                                baseItemDetails.setPriceDenomination(invoiceLineType.getPrice().getPriceAmount().getCurrencyID());
                                baseItemDetails.setLineItemGrossAmount(Float.valueOf(invoiceLineType.getLineExtensionAmount().getValue().toString()));
                                if(null != invoiceLineType.getInvoicePeriods()
                                        && IMConstants.ZERO != invoiceLineType.getInvoicePeriods().size()) {
                                    baseItemDetails.setStartDate(invoiceLineType.getInvoicePeriods().get(0).getStartDate().getValue());
                                    baseItemDetails.setEndDate(invoiceLineType.getInvoicePeriods().get(0).getEndDate().getValue());
                                }
                                baseItemDetailsList.add(baseItemDetails);
                            }

                            nettleie.setBaseItemDetails(baseItemDetailsList);
                            attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder().setNettleie(nettleie);
                            break;

                        } else if(IMConstants.PDFE2B.equals(pdfAttachment.getFAKTURA().getVEDLEGGFORMAT())) {
                            nettleie = new Nettleie();
                            no.fjordkraft.im.if320.models.Invoice invoice = pdfAttachment.getFAKTURA().getVedlegge2BObj().getInvoice();

                            nettleie.setReferenceNumber(String.valueOf(invoice.getInvoiceHeader().getInvoiceNumber()));
                            nettleie.setBaseItemDetails(invoice.getInvoiceDetails().getBaseItemDetails());
                            nettleie.setInvoiceSummary(invoice.getInvoiceSummary());

                            attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder().setNettleie(nettleie);
                            break;
                        }
                    }
                }
                attachmentList.add(attachment);
            }
        }
        attachments.setAttachment(attachmentList);
        request.getStatement().setTotalAttachment(attachmentList.size());
    }
}
