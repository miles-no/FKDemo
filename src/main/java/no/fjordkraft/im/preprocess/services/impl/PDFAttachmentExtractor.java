package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Attachment;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.repository.InvoicePdfRepository;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bhavi on 5/8/2017.
 */

@Service
@PreprocessorInfo(order=2)
public class PDFAttachmentExtractor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(PDFAttachmentExtractor.class);

    @Autowired
    private InvoicePdfRepository invoicePdfRepository;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {
        Statement stmt = request.getStatement();
        int count = 0;
        for(Attachment attachment : stmt.getAttachments().getAttachment()){
            if("PDF".equals(attachment.getFAKTURA().getVEDLEGGFORMAT()) || "PDFEHF".equals(attachment.getFAKTURA().getVEDLEGGFORMAT()) || "PDFE2B".equals(attachment.getFAKTURA().getVEDLEGGFORMAT())){
                InvoicePdf invoicePdf = new InvoicePdf();
                byte[] decoded = Base64.decodeBase64(attachment.getFAKTURA().getVEDLEGGPDF().getBytes());
                invoicePdf.setPayload(decoded);
                invoicePdf.setType(attachment.getFAKTURA().getVEDLEGGFORMAT());
                invoicePdf.setStatement(request.getEntity());
                invoicePdf = invoicePdfRepository.saveAndFlush(invoicePdf);
                count++;
                logger.debug("Save pdf of type "+invoicePdf.getType() + " with id "+invoicePdf.getId() + " statement id "+request.getEntity().getId() );
            }
        }
        request.getEntity().setPdfAttachment(count);
    }

    public InvoicePdfRepository getInvoicePdfRepository() {
        return invoicePdfRepository;
    }

    public void setInvoicePdfRepository(InvoicePdfRepository invoicePdfRepository) {
        this.invoicePdfRepository = invoicePdfRepository;
    }
}
