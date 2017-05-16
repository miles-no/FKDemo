package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Attachment;
import no.fjordkraft.im.if320.models.FAKTURA;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.services.PreprocessorEngine;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by bhavi on 5/8/2017.
 */
@Service
public class PreprocessorServiceImpl implements PreprocessorService {

    private static final Logger logger = LoggerFactory.getLogger(PreprocessorServiceImpl.class);

    @Autowired
    Unmarshaller unMarshaller;

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    PreprocessorEngine preprocessorEngine;

    @Override
    public Statement unmarshallStatement(String path) {
        try {
            InputStream inputStream = new FileInputStream(path);
            return unmarshallStatement(inputStream);
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Statement unmarshallStatement(InputStream inputStream) {
        try {
            //InputStream inputStream = new FileInputStream("path");
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1);
            StreamSource source = new StreamSource(reader);
            System.out.println(unMarshaller.supports(Statement.class));
            Statement stmt = (Statement)unMarshaller.unmarshal(source);
            System.out.println(stmt);
            unmarshallAttachments(stmt);
            return stmt;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
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

    private void decodeAndUnmarshalEHFAttachment(Statement statement){
        for(Attachment attachment : statement.getAttachments().getAttachment()){

        }
    }

    @Transactional
    public void preprocess() throws IOException {
        StopWatch stopwatch = new StopWatch("Preprocessing");
        stopwatch.start();
        List<no.fjordkraft.im.model.Statement> statementList = statementRepository.readStatements(StatementStatusEnum.PENDING.name());
        for(no.fjordkraft.im.model.Statement statement:statementList) {
            logger.debug("Preprocessing statement with id "+ statement.getId() );
            String payload = statement.getPayload();
            statement.getSystemBatchInput().getFilename();
            Statement if320statement = unmarshallStatement(new ByteArrayInputStream(payload.getBytes(StandardCharsets.ISO_8859_1)));
            updateStatementsInDatabase(if320statement,statement);
            PreprocessRequest<Statement,no.fjordkraft.im.model.Statement> request = new PreprocessRequest();
            request.setStatement(if320statement);
            request.setEntity(statement);
            preprocessorEngine.execute(request);
            statement.setStatus(StatementStatusEnum.PRE_PROCESSED.getStatus());
            statementRepository.saveAndFlush(statement);
        }
        stopwatch.stop();
        logger.debug("Time for preprocessing "+statementList.size() + " " + stopwatch.prettyPrint());
    }

    public void updateStatementsInDatabase(Statement statement,no.fjordkraft.im.model.Statement statementEntity){
        Long statementOcr = statement.getStatementOcrNumber();
        Integer customerId = statement.getNationalId();
        Long accountNumber = statement.getAccountNumber();
        String invoiceNumber = accountNumber + ""+ statement.getSequenceNumber();
        statementEntity.setStatementId(statementOcr.toString());
        statementEntity.setCustomerId(customerId.toString());
        statementEntity.setAccountNumber(accountNumber.toString());
        statementEntity.setInvoiceNumber(invoiceNumber);
        statementEntity.setStatus(StatementStatusEnum.PRE_PROCESSING.getStatus());

        logger.debug("updating statement  "+ statementEntity.getId() + " statementOcr " + statementOcr + " customerId " + customerId + " accountNumber "+ accountNumber + " invoiceNumber "+  invoiceNumber );
        statementRepository.saveAndFlush(statementEntity);
    }
}
