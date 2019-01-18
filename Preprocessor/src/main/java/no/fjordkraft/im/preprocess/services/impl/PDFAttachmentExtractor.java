package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Attachment;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.InvoiceService;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bhavi on 5/8/2017.
 */

@Service
@PreprocessorInfo(order=5,skipOnline = true)
public class PDFAttachmentExtractor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(PDFAttachmentExtractor.class);

    @Autowired
    private InvoiceService invoiceService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {
        Statement stmt = request.getStatement();
        int count = 0;
        for(Attachment attachment : stmt.getAttachments().getAttachment()){
                if("PDF".equals(attachment.getFAKTURA().getVEDLEGGFORMAT()) || "PDFEHF".equals(attachment.getFAKTURA().getVEDLEGGFORMAT()) || "PDFE2B".equals(attachment.getFAKTURA().getVEDLEGGFORMAT())){
                    if(configService.getBoolean(IMConstants.SAVE_INVOICE_PDF_IN_DB)) {
                    InvoicePdf invoicePdf = new InvoicePdf();
                    byte[] decoded = Base64.decodeBase64(attachment.getFAKTURA().getVEDLEGGPDF().getBytes());
                    invoicePdf.setPayload(decoded);
                    invoicePdf.setType(attachment.getFAKTURA().getVEDLEGGFORMAT());
                    invoicePdf.setStatement(request.getEntity());
                    invoicePdf = invoiceService.saveInvoicePdf(invoicePdf);
                     }
                    count++;
                    if(!request.getEntity().isE2bAttachment() && "PDFE2B".equals(attachment.getFAKTURA().getVEDLEGGFORMAT()))  {
                        request.getEntity().setE2bAttachment(true);
                    }
                    if(!request.getEntity().isEhfAttachment() && "PDFEHF".equals(attachment.getFAKTURA().getVEDLEGGFORMAT()))  {
                        request.getEntity().setEhfAttachment(true);
                    }

                    //logger.debug("Save pdf of type "+invoicePdf.getType() + " with id "+invoicePdf.getId() + " statement id "+request.getEntity().getId() );
                }
        }
        request.getEntity().setPdfAttachment(count);
    }

    public InvoiceService getInvoiceService() {
        return invoiceService;
    }

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
}
