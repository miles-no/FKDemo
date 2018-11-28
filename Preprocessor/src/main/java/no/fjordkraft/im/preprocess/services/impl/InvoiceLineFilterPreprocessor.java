package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.AuditLogService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 11/21/18
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
@PreprocessorInfo(order=115)
public class InvoiceLineFilterPreprocessor  extends BasePreprocessor  {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceLineFilterPreprocessor.class);

    @Autowired
    AuditLogService auditLogService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request)
    {
        try
        {
            Statement stmt = request.getStatement();
            List<Attachment> attachmentList = stmt.getAttachments().getAttachment();
            logger.debug("LegalPartClass = " + stmt.getLegalPartClass());
            if(attachmentList!=null && attachmentList.size()>0)
            {
                for(Attachment attachment: attachmentList)
                {
                    if(attachment.getFAKTURA()!=null && IMConstants.EMUXML.equals(attachment.getFAKTURA().getVEDLEGGFORMAT()))
                    {

                        List<InvoiceOrder> invoiceOrders = new ArrayList<InvoiceOrder>();
                        no.fjordkraft.im.if320.models.Invoice invoice = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice();
                        logger.debug("meter ID " + attachment.getFAKTURA().getMAALEPUNKT());
                         //In case of Organization there could be multiple invoice orders.
                        if(invoice.getInvoiceOrder()!=null && invoice.getInvoiceOrder().size() > 0)
                        {
                            logger.debug("No Of Invoice Orders " + invoice.getInvoiceOrder().size());
                            for(InvoiceOrder invoiceOrder :invoice.getInvoiceOrder())
                            {
                                List<InvoiceLine120> invoiceLine120List = new ArrayList<InvoiceLine120>();
                                if(invoiceOrder.getInvoiceLine120()!=null && invoiceOrder.getInvoiceLine120().size()>0)
                                {
                                    logger.debug("No Of Invoice Lines before filtering " + invoiceOrder.getInvoiceLine120().size());
                                    for (InvoiceLine120 invoiceLine120 : invoiceOrder.getInvoiceLine120())
                                    {
                                        if(invoiceLine120.getProdId()!=null && !invoiceLine120.getProdId().trim().contains(IMConstants.INVOICE_LINE_TAG_DUMMY))
                                        {
                                            invoiceLine120List.add(invoiceLine120);
                                        } else if(invoiceLine120.getProdId().trim().contains(IMConstants.INVOICE_LINE_TAG_DUMMY))
                                        {
                                            String description = invoiceOrder.getProductParameters118().getDescription();
                                            logger.debug("Description for the invoice number " + request.getStatement().geteInvoiceId() + " is " + description) ;
                                            if(description!=null && !description.isEmpty()&& description.toUpperCase().contains(IMConstants.INVOICE_GRID_DESCRIPTION_DUMMY))
                                            {
                                                invoiceOrder.getProductParameters118().setDescription(null);
                                            }
                                        }
                                    }
                                    logger.debug("No Of Invoice Lines after filtering " + invoiceLine120List.size());
                                    invoiceOrder.setInvoiceLine120(invoiceLine120List);
                                }
                                invoiceOrders.add(invoiceOrder);
                            }
                            logger.debug("No Of Invoice Orders " + invoiceOrders.size());
                            attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().setInvoiceOrder(invoiceOrders);
                        }
                    }
                }
            }

        }catch(Exception e) {
            String message = "Error While filtering InvoiceLine.";
            logger.debug("Exception in Invoice Line Preprocessor", e);
            auditLogService.saveAuditLog(request.getEntity().getId(), StatementStatusEnum.PRE_PROCESSING_FAILED.getStatus(),message, IMConstants.INFO,request.getEntity().getLegalPartClass());
            throw new PreprocessorException(e);
        }
    }
}
