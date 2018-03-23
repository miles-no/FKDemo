package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.util.IMConstants;
import no.fjordkraft.im.util.SetInvoiceASOnline;
import oasis.names.specification.ubl.schema.xsd.creditnote_2.CreditNote;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("unmarshaller")
    private Unmarshaller unMarshaller;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {

        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("Unmarshall Attachments");
            if(SetInvoiceASOnline.get() == null || !SetInvoiceASOnline.get()) {
            createDirectories(request);
            }
            unmarshallAttachments(request.getStatement());
            decodeAndUnmarshalEHFAttachment(request.getStatement());
            stopWatch.stop();

            logger.debug("TIme taken for unmarshalling of attachment of statement with id  " + request.getEntity().getId() + stopWatch.prettyPrint());
        } catch (Exception e) {
            logger.error("Exception in generic preprocessor",e);
            throw new PreprocessorException(e);
        }
    }



    public Statement unmarshallAttachments(Statement statement) throws IOException {
        if(null != statement.getAttachments() && null != statement.getAttachments().getAttachmentList()) {
            for (String data : statement.getAttachments().getAttachmentList()) {
                data = data.replaceAll("&lt;!\\[CDATA\\[", "");
                data = data.replaceAll("\\]\\]&gt;", "");
                if(!data.contains("VEDLEGG_EHF")) {
                    data = data.replaceAll("&", "&amp;");
                }
                data = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" + data;
                logger.debug("attachment is "+data.getBytes(StandardCharsets.ISO_8859_1));
                StreamSource source = new StreamSource(new ByteArrayInputStream(data.getBytes(StandardCharsets.ISO_8859_1)));
                FAKTURA faktura = (FAKTURA) unMarshaller.unmarshal(source);
                Attachment attachment = new Attachment();
                attachment.setFAKTURA(faktura);
                statement.getAttachments().getAttachment().add(attachment);
            }
            statement.getAttachments().setAttachmentList(null);
        }

        return statement;
    }

    public void decodeAndUnmarshalEHFAttachment(Statement statement) throws IOException {
        for(Attachment attachment : statement.getAttachments().getAttachment()){
            if("PDFEHF".equals(attachment.getFAKTURA().getVEDLEGGFORMAT())) {
                String data = attachment.getFAKTURA().getVedleggehf();
                byte[] decoded = null;
                if(null != data) {
                    decoded = Base64.decodeBase64(data);
                    StreamSource source = new StreamSource(new InputStreamReader(new ByteArrayInputStream(decoded), StandardCharsets.ISO_8859_1));
                    try {

                        VEDLEGGEHF ehf = new VEDLEGGEHF();
                        if("creditnote".equalsIgnoreCase(attachment.getFAKTURA().getFAKTURATYPE().toLowerCase()))
                        {
                           oasis.names.specification.ubl.schema.xsd.creditnote_2.CreditNote creditNote = (CreditNote) unMarshaller.unmarshal(source);
                           ehf.setCreditNote(creditNote);
                           ehf.setType("creditnote");
                           attachment.getFAKTURA().setVedleggehfObj(ehf);
                        }
                        else
                        {
                            oasis.names.specification.ubl.schema.xsd.invoice_2.Invoice invoice = (oasis.names.specification.ubl.schema.xsd.invoice_2.Invoice) unMarshaller.unmarshal(source);
                            ehf.setInvoice(invoice);
                            ehf.setType("invoice");
                            attachment.getFAKTURA().setVedleggehfObj(ehf);
                        }
                        attachment.getFAKTURA().setVedleggehf(null);
                    } catch(Exception e) {

                            e.printStackTrace();
                            //throw e;
                        }
                }
            } else if("PDFE2B".equals(attachment.getFAKTURA().getVEDLEGGFORMAT())){
                String data = attachment.getFAKTURA().getVedlegge2B();
                if(null != data) {
                    if(data.endsWith("&gt;")) {
                        data = data.replaceAll("&gt;","");
                    }
                    byte[] decoded = Base64.decodeBase64(data);
                   decoded = new String(decoded,StandardCharsets.ISO_8859_1).replaceAll("&", "&amp;").getBytes(StandardCharsets.ISO_8859_1);
                   // logger.debug("New Decoded file " + new String(decoded));

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

    public void setUnMarshaller(Unmarshaller unMarshaller) {
        this.unMarshaller = unMarshaller;
    }
}
