package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Attachment;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Iterator;

/**
 * Created by bhavi on 5/8/2017.
 */

@PreprocessorInfo(order=3)
@Service
public class PDFAttachmentRemover extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(PDFAttachmentRemover.class);

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {

        if(null != request.getStatement().getAttachments() && null != request.getStatement().getAttachments().getAttachment()) {
            Iterator<Attachment> iterator = request.getStatement().getAttachments().getAttachment().iterator();
            while (iterator.hasNext()) {
                Attachment attachment = iterator.next();
                if ("PDF".equals(attachment.getFAKTURA().getVEDLEGGFORMAT())) {
                    iterator.remove();
                } else if ("PDFEHF".equals(attachment.getFAKTURA().getVEDLEGGFORMAT()) || "PDFE2B".equals(attachment.getFAKTURA().getVEDLEGGFORMAT())) {
                    attachment.getFAKTURA().setVEDLEGGPDF(null);

                }
            }
        }
    }
}
