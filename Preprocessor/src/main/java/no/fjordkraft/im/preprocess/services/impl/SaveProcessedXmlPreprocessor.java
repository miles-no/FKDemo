package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by bhavi on 5/15/2017.
 */
@Service
@PreprocessorInfo(order=999,skipOnline = false)
public class SaveProcessedXmlPreprocessor  extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(SaveProcessedXmlPreprocessor.class);

    @Autowired
    @Qualifier("marshaller")
    Marshaller marshaller;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {

        try {
            logger.debug("Credit limit for statement with statement id  "+request.getEntity().getId() + " is "+request.getStatement().getAccountStatement().getStatementSummary().getCreditAmount());

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

        }*/

            baseFile.mkdirs();
            request.setPathToProcessedXml(baseFile.getAbsolutePath());
            logger.debug("Generated base File " + baseFile);
            File file  = new File(request.getPathToProcessedXml());
            if(!file.exists()) {
                logger.debug("file does not exists create directories" + file.getAbsolutePath());
                createDirectories(request);
            }
            StreamResult streamResult = new StreamResult(new FileOutputStream(request.getPathToProcessedXml() + File.separator + IMConstants.PROCESSED_STATEMENT_XML_FILE_NAME));
            marshaller.marshal(request.getStatement(), streamResult);
            if(null != streamResult.getOutputStream()) {
                logger.debug("closing stream in SaveProcessedXmlPreprocessor");
                streamResult.getOutputStream().close();
            }
        }catch (Exception e) {
            logger.error("Exception in save processed xml",e);
            throw new PreprocessorException("Exception while saving processed xml",e);
        }
    }
}
