package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Attachment;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

/**
 * Created by bhavi on 9/14/2017.
 */
@Service
public class UtilityService {

    private static final Logger logger = LoggerFactory.getLogger(UtilityService.class);

    @Autowired
    @Qualifier("unmarshaller")
    private Unmarshaller unMarshaller;

    @Autowired
    @Qualifier("marshaller")
    Marshaller marshaller;

    @Autowired
    GenericPreprocessor preprocessor;

    @Autowired
    PreprocessorService preprocessorService;

    public void decodeEHFE2B(String srcPath, String destPath) throws Exception {
        try {
            Statement statement = preprocessorService.unmarshallStatement(srcPath);
            preprocessor.unmarshallAttachments(statement);
            preprocessor.decodeAndUnmarshalEHFAttachment(statement);
            removeAttachmentPDF(statement);
            File file  = new File(destPath);
            if(!file.exists()) {
                logger.debug("file does not exists create directories" + file.getAbsolutePath());
                file.mkdirs();
            }

            StreamResult streamResult = new StreamResult(new FileOutputStream(destPath + File.separator + "statement.xml"));
            marshaller.marshal(statement, streamResult);
            if(null != streamResult.getOutputStream()) {
                logger.debug("closing stream in ");
                streamResult.getOutputStream().close();
            }

        } catch (Exception e) {
            logger.debug("",e);
            throw e;
        }

    }

    private void removeAttachmentPDF(Statement statement){
        if(null != statement.getAttachments() && null != statement.getAttachments().getAttachment()) {
            Iterator<Attachment> iterator = statement.getAttachments().getAttachment().iterator();
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
