package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.Attachment;
import no.fjordkraft.im.if320.models.BaseItemDetails;
import no.fjordkraft.im.if320.models.InvoiceLine120;
import no.fjordkraft.im.if320.models.Statement;
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
@PreprocessorInfo(order=24)
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
            if(attachmentList!=null && attachmentList.size()>0)
            {
                for(Attachment attachment: attachmentList)
                {
                    if(IMConstants.EMUXML.equals(attachment.getFAKTURA().getVEDLEGGFORMAT()))
                    {
                        List<InvoiceLine120> invoiceLine120List = new ArrayList<InvoiceLine120>();
                        no.fjordkraft.im.if320.models.Invoice invoice = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice();
                        if(invoice.getInvoiceFinalOrder().getInvoiceLine120()!=null && invoice.getInvoiceFinalOrder().getInvoiceLine120().size() > 0)
                        {
                            for (InvoiceLine120 invoiceLine120 : invoice.getInvoiceFinalOrder().getInvoiceLine120())
                            {
                                if(invoiceLine120.getProdId()!=null && !invoiceLine120.getProdId().trim().contains(IMConstants.INVOICE_LINE_TAG_DUMMY))
                                {
                                    invoiceLine120List.add(invoiceLine120);
                                } else if(invoiceLine120.getProdId().trim().contains(IMConstants.INVOICE_LINE_TAG_DUMMY))
                                {
                                    String description = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getProductParameters118().getDescription();
                                    logger.debug("Description for the invoice number " + request.getStatement().geteInvoiceId() + " is " + description) ;
                                    if(description!=null && !description.isEmpty()&& description.toUpperCase().contains(IMConstants.INVOICE_GRID_DESCRIPTION_DUMMY)) {
                                     attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getProductParameters118().setDescription(null);
                                    }
                                }
                            }
                            logger.debug("");
                            attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setInvoiceLine120(invoiceLine120List);
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
