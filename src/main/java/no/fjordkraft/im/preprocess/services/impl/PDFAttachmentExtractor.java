package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Attachment;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.preprocess.services.Preprocessor;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;


import no.fjordkraft.im.repository.SystemConfigRepository;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by bhavi on 5/8/2017.
 */

@Service
@PreprocessorInfo(order=2)
public class PDFAttachmentExtractor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(PDFAttachmentExtractor.class);


    @Value("${base.directory.path}")
    private String baseDirectoryPath;

    private SystemConfigRepository systemConfigRepository;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {
        Statement stmt = request.getStatement();
        FileOutputStream fos =  null;
        int index = 1;
        for(Attachment attachment : stmt.getAttachments().getAttachment()){
            if("PDF".equals(attachment.getFAKTURA().getVEDLEGGFORMAT())){
                try {
                    fos = new FileOutputStream(new File(new File(request.getPathToAttachmentPDF()),request.getEntity().getInvoiceNumber()+"_"+ (index++) +".pdf"));
                    byte[] decoded = Base64.decodeBase64(attachment.getFAKTURA().getVEDLEGGPDF().getBytes());
                    fos.write(decoded);
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /*private String getPath() {
        String destinationPath = systemConfigRepository.getConfigValue(DESTINATION_PATH);
        String folderName = filename.substring(0, filename.indexOf('.'));
        new File(destinationPath + folderName).mkdir();
        String pdfGeneratedFolderName = systemConfigRepository.getConfigValue(PDF_GENERATED_FOLDER_NAME);
    }*/
}
