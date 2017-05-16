package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.repository.SystemConfigRepository;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.pkcs11.wrapper.Constants;

import java.io.File;

/**
 * Created by bhavi on 5/11/2017.
 */

@Service
@PreprocessorInfo(order=1)
public class GenericPreprocessor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(PDFAttachmentExtractor.class);

    @Autowired
    public SystemConfigRepository configRepository;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {

        String invoiceNumber = request.getEntity().getInvoiceNumber();
        String baseFolder = request.getEntity().getSystemBatchInput().getFilename();
        String folderName = baseFolder.substring(0, baseFolder.indexOf('.'));
        String basePath = configRepository.getConfigValue(IMConstants.DESTINATION_PATH);
        String pdfGeneratedFolderName = configRepository.getConfigValue(IMConstants.PDF_GENERATED_FOLDER_NAME);

        File baseFile = new File(basePath + folderName + File.separator + invoiceNumber);
        baseFile.mkdir();
        File generatedPDFFile = new File(baseFile, pdfGeneratedFolderName);
        generatedPDFFile.mkdir();

        String processedXmlFolderName  = configRepository.getConfigValue(IMConstants.PROCESSED_XML_FOLDER_NAME);
        File processedXmlFile = new File(baseFile,processedXmlFolderName);
        processedXmlFile.mkdir();
        request.setPathToProcessedXml(processedXmlFile.getAbsolutePath());

        logger.debug("generatedPDFFolder "+ generatedPDFFile.getAbsolutePath() + " attachmentPDFFile "  + processedXmlFile + processedXmlFile.getAbsolutePath() );
    }


}
