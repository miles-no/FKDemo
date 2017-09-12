package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.preprocess.services.Preprocessor;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * Created by bhavi on 5/8/2017.
 */
public abstract class BasePreprocessor implements Preprocessor {

    private static final Logger logger = LoggerFactory.getLogger(BasePreprocessor.class);

    @Autowired
    protected ConfigService configService;

    protected boolean canProcess(PreprocessRequest request){
        PreprocessorInfo annotationObj1 = this.getClass().getAnnotation(PreprocessorInfo.class);
        return true;
    }

    @Override
    public int compareTo(Preprocessor preprocessor){
        PreprocessorInfo annotationObj1 = this.getClass().getAnnotation(PreprocessorInfo.class);
        Integer order1 = annotationObj1.order();
        PreprocessorInfo annotationObj2 = preprocessor.getClass().getAnnotation(PreprocessorInfo.class);
        Integer order2 = annotationObj2.order();
        return order1.compareTo(order2);
    }

    protected void createDirectories(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request){
        String invoiceNumber = request.getEntity().getInvoiceNumber();
        String baseFolder = request.getEntity().getSystemBatchInput().getTransferFile().getFilename();
        String folderName = baseFolder.substring(0, baseFolder.indexOf('.'));
        String basePath = configService.getString(IMConstants.BASE_DESTINATION_FOLDER_PATH);
        String pdfGeneratedFolderName = configService.getString(IMConstants.GENERATED_PDF_FOLDER_NAME);
        String mergePdfFolderName = configService.getString(IMConstants.GENERATED_INVOICE_FOLDER_NAME);

        File baseFile = new File(basePath + folderName + File.separator + invoiceNumber);
        baseFile.mkdir();
        File generatedPDFFile = new File(baseFile, pdfGeneratedFolderName);
        generatedPDFFile.mkdirs();

        String processedXmlFolderName = configService.getString(IMConstants.PROCESSED_XML_FOLDER_NAME);
        File processedXmlFile = new File(baseFile, processedXmlFolderName);
        processedXmlFile.mkdirs();

        File mergePdfFile = new File(baseFile, mergePdfFolderName);
        mergePdfFile.mkdirs();

        request.setPathToProcessedXml(processedXmlFile.getAbsolutePath());
        logger.debug("generatedPDFFolder " + generatedPDFFile.getAbsolutePath() + " attachmentPDFFile " + processedXmlFile.getAbsolutePath());
    }

}
