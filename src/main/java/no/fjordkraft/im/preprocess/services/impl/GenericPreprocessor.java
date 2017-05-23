package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.repository.SystemConfigRepository;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Created by bhavi on 5/11/2017.
 */

@Service
@PreprocessorInfo(order=1)
public class GenericPreprocessor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(PDFAttachmentExtractor.class);

    @Autowired
    public SystemConfigRepository configRepository;

    @Autowired
    Unmarshaller unMarshaller;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {

        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("Unmarshall Attachments");
            String invoiceNumber = request.getEntity().getInvoiceNumber();
            String baseFolder = request.getEntity().getSystemBatchInput().getFilename();
            String folderName = baseFolder.substring(0, baseFolder.indexOf('.'));
            String basePath = configRepository.getConfigValue(IMConstants.DESTINATION_PATH);
            String pdfGeneratedFolderName = configRepository.getConfigValue(IMConstants.PDF_GENERATED_FOLDER_NAME);

            File baseFile = new File(basePath + folderName + File.separator + invoiceNumber);
            baseFile.mkdir();
            File generatedPDFFile = new File(baseFile, pdfGeneratedFolderName);
            generatedPDFFile.mkdir();

            String processedXmlFolderName = configRepository.getConfigValue(IMConstants.PROCESSED_XML_FOLDER_NAME);
            File processedXmlFile = new File(baseFile, processedXmlFolderName);
            processedXmlFile.mkdir();
            request.setPathToProcessedXml(processedXmlFile.getAbsolutePath());
            unmarshallAttachments(request.getStatement());
            decodeAndUnmarshalEHFAttachment(request.getStatement());
            stopWatch.stop();
            logger.debug("generatedPDFFolder " + generatedPDFFile.getAbsolutePath() + " attachmentPDFFile " + processedXmlFile + processedXmlFile.getAbsolutePath());
            logger.debug("TIme taken for unmarshalling of attachment of statement with id  " + request.getEntity().getId() + stopWatch.prettyPrint());
        } catch (Exception e) {
            throw new PreprocessorException(e);
        }
    }


    private Statement unmarshallAttachments(Statement statement) throws IOException {
        for(String data:statement.getAttachments().getAttachmentList()){
            data = data.replaceAll("&lt;!\\[CDATA\\[", "");
            data = data.replaceAll("\\]\\]&gt;","" );
            data = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" + data;
            //Unmarshaller unmarshaller =  jaxbContext2.createUnmarshaller();
            StreamSource source = new StreamSource(new ByteArrayInputStream(data.getBytes(StandardCharsets.ISO_8859_1)));
            FAKTURA faktura = (FAKTURA)unMarshaller.unmarshal(source);
            Attachment attachment = new Attachment();
            attachment.setFAKTURA(faktura);
            statement.getAttachments().getAttachment().add(attachment);
            System.out.println(faktura);
        }
        statement.getAttachments().setAttachmentList(null);
        return statement;
    }

    private void decodeAndUnmarshalEHFAttachment(Statement statement) throws IOException {
        for(Attachment attachment : statement.getAttachments().getAttachment()){
            if("PDFEHF".equals(attachment.getFAKTURA().getVEDLEGGFORMAT())) {
                String data = attachment.getFAKTURA().getVedleggehf();
                if(null != data) {
                    byte[] decoded = Base64.decodeBase64(data);
                    logger.debug("decoded ehf is " +new String(decoded));
                    StreamSource source = new StreamSource(new InputStreamReader(new ByteArrayInputStream(decoded),StandardCharsets.ISO_8859_1));
                    oasis.names.specification.ubl.schema.xsd.invoice_2.Invoice invoice = (oasis.names.specification.ubl.schema.xsd.invoice_2.Invoice)unMarshaller.unmarshal(source);
                    VEDLEGGEHF ehf = new VEDLEGGEHF();
                    ehf.setInvoice(invoice);
                    attachment.getFAKTURA().setVedleggehfObj(ehf);
                    attachment.getFAKTURA().setVedleggehf(null);
                }
            } else if("PDFE2B".equals(attachment.getFAKTURA().getVEDLEGGFORMAT())){
                String data = attachment.getFAKTURA().getVedlegge2B();
                if(null != data) {
                    byte[] decoded = Base64.decodeBase64(data);
                    StreamSource source = new StreamSource(new InputStreamReader(new ByteArrayInputStream(decoded),StandardCharsets.ISO_8859_1));
                    Invoice invoice = (Invoice)unMarshaller.unmarshal(source);
                    VEDLEGGE2B e2b = new VEDLEGGE2B();
                    e2b.setInvoice(invoice);
                    attachment.getFAKTURA().setVedlegge2BObj(e2b);
                    attachment.getFAKTURA().setVedlegge2B(null);
                }
            }
        }
    }

}
