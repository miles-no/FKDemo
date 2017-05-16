package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.repository.SystemConfigRepository;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by bhavi on 5/15/2017.
 */
@Service
@PreprocessorInfo(order=100)
public class SaveProcessedXmlPreprocessor  extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(PDFAttachmentRemover.class);

    @Autowired
    @Qualifier("marshaller")
    Marshaller marshaller;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) throws IOException {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("SaveProcessedXmlPreprocessor");
        StreamResult streamResult = new StreamResult(new FileOutputStream(request.getPathToProcessedXml()+File.separator+ "statement.xml"));
        marshaller.marshal(request.getStatement(),streamResult);
        stopWatch.stop();
        logger.debug(stopWatch.prettyPrint());


    }


}
