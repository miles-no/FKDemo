package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.preprocess.services.Preprocessor;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.util.IMConstants;
//import no.fjordkraft.im.util.SetInvoiceASOnline;
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
        String invoiceNumber = request.getEntity().getAccountNumber();
        String basePath = configService.getString(IMConstants.BASE_DESTINATION_FOLDER_PATH);
        File baseFile = null;
        //if(SetInvoiceASOnline.get()==null || !SetInvoiceASOnline.get())  {
        //if(!request.getStatement().isOnline()) {
        String baseFolder = request.getEntity().getSystemBatchInput().getTransferFile().getFilename();
        String folderName = baseFolder.substring(0, baseFolder.indexOf('.'));
        baseFile = new File(basePath + folderName + File.separator + invoiceNumber);
       /*}
        else
        {
            baseFile =  new File(basePath + "Online" + File.separator+ invoiceNumber);

        }*//*

            baseFile.mkdirs();
            request.setPathToProcessedXml(baseFile.getAbsolutePath());
            logger.debug("Generated base File " + baseFile);
        } */
    }
}
