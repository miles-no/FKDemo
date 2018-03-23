package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.model.StatementPayload;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.model.TransferFile;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import no.fjordkraft.im.services.*;
import no.fjordkraft.im.statusEnum.SystemBatchInputStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import no.fjordkraft.im.util.SetInvoiceASOnline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.Marshaller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavi on 9/14/2017.
 */

@RestController
@RequestMapping("/api")
public class ProcessInvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(PreprocessController.class);

    @Autowired
    PreprocessorService preprocessorService;


    @Autowired
    @Qualifier("marshaller")
    Marshaller marshaller;

    @Autowired
    PDFGenerator pdfGenerator;

    @Autowired
    ConfigService configService;

    @RequestMapping(value = "/online/xmlGenerator",method= RequestMethod.POST,consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<byte[]> preprocessXml(@RequestParam(value = "file", required = false) MultipartFile file) {
       try {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start("Processing online file started for file " + file.getOriginalFilename());
                InputStream inputStream = file.getInputStream();

                TransferFile transferFile = new TransferFile();
                transferFile.setFilename(file.getOriginalFilename());
                transferFile.setImStatus("1");

                SystemBatchInput systemBatchInput = new SystemBatchInput();
                systemBatchInput.setStatus(SystemBatchInputStatusEnum.PROCESSING.getStatus());
                systemBatchInput.setTransferFile(transferFile);

                XMLInputFactory factory = XMLInputFactory.newInstance();
                XMLEventReader eventReader = factory.createXMLEventReader(inputStream);

                Statement statement = splitFileAndGetFirstStatement(eventReader,systemBatchInput);
                statement.setInvoiceNumber(statement.getAccountNumber()+statement.getSeqNo());
                statement.setOnline(Boolean.TRUE);

                String filename = file.getOriginalFilename().replace(".xml", "");
                String basePath = configService.getString(IMConstants.BASE_DESTINATION_FOLDER_PATH);
                File baseFile = new File(basePath +"Online"+ File.separator +filename+File.separator+ statement.getInvoiceNumber());
                baseFile.mkdirs();

                SetInvoiceASOnline.set(Boolean.TRUE);
                preprocessorService.preprocess(statement);

                StreamResult streamResult = new StreamResult(new FileOutputStream(baseFile+File.separator +IMConstants.PROCESSED_STATEMENT_XML_FILE_NAME));
                marshaller.marshal(preprocessorService.getStatement(), streamResult);
                if(null != streamResult.getOutputStream()) {
                    logger.debug("closing stream in Process Invoice Controller");
                    streamResult.getOutputStream().close();
                }

                SetInvoiceASOnline.set(Boolean.TRUE);
                byte[] generatedPDF = pdfGenerator.generateInvoiceForSingleStatement(baseFile+File.separator +IMConstants.PROCESSED_STATEMENT_XML_FILE_NAME,statement.getBrand(),statement.getLayoutID());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentLength(generatedPDF.length);
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample.pdf");
                SetInvoiceASOnline.unset();
                stopWatch.stop();
                logger.debug(stopWatch.prettyPrint());
                return new ResponseEntity<byte[]>(generatedPDF, headers, HttpStatus.OK);
            } catch (Exception e) {

                logger.debug("exception while processing online file " + file.getOriginalFilename() ,e);
            }
          return null;
       }

    private Statement splitFileAndGetFirstStatement(XMLEventReader eventReader,SystemBatchInput systemBatchInput) throws Exception {

        Statement imStatement;
        XMLEventWriter writer = null;
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        boolean isStatementOcr = false;
        boolean isNumberOfRecords = false;
        boolean isBrandCode = false;
        boolean isAcctNumber = false;
        boolean isSeqNo = false;
        String invoiceNumber = "";
        boolean isStopProcessing = false;

        imStatement = new Statement();
      //  imStatement.setSystemBatchInput(systemBatchInput);
        StringWriter stringOut = null;
        while (eventReader.hasNext() && !isStopProcessing) {
            XMLEvent event = eventReader.nextEvent();
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    //stringOut = new StringWriter();
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();
                    if(("BrandCode").equals(qName))
                    {
                         isBrandCode = true;
                    }
                    if (("Statement").equalsIgnoreCase(qName)) {
                        stringOut = new StringWriter();
                        writer = outputFactory
                                .createXMLEventWriter(stringOut);
                        writer.add(event);
                    } else if (qName.equalsIgnoreCase("StatementOcrNumber")) {
                        isStatementOcr = true;
                        writer.add(event);
                    } else if (qName.equalsIgnoreCase("NumberOfRecords")) {
                        isNumberOfRecords = true;
                    }else if (qName.equalsIgnoreCase("SequenceNumber") ) {
                        isSeqNo = true;
                        writer.add(event);
                    }  else if (qName.equalsIgnoreCase(("AccountNumber")))  {
                        isAcctNumber = true;
                        writer.add(event);
                    }

                    else if (writer != null) {
                        writer.add(event);
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();

                    if ("Statement".equalsIgnoreCase(endElement.getName().getLocalPart())) {
                        writer.add(event);
                        writer.flush();
                        writer.close();
                        writer = null;
                        //imStatement.setStatementType(brandCode);
                        logger.debug("saving statement with ocr"+imStatement.getStatementId());
                        StatementPayload statementPayload = new StatementPayload();
                        statementPayload.setPayload(stringOut.toString());
                        statementPayload.setStatement(imStatement);

                        imStatement.setStatementPayload(statementPayload);
                        systemBatchInput.setNumOfRecords(1);
                        imStatement.setSystemBatchInput(systemBatchInput);
                       // statementService.saveIMStatementinDB(stringOut.toString(), imStatement);
                        stringOut.getBuffer().setLength(0);
                        isStopProcessing = true;
                        break;

                    } else if (writer != null) {
                        writer.add(event);
                    }

                    break;

                case XMLStreamConstants.CHARACTERS:
                    if (isStatementOcr) {
                        Characters characters = event.asCharacters();
                        imStatement.setStatementId(characters.getData());
                        writer.add(event);
                        isStatementOcr = false;
                    }else if(isBrandCode) {
                        Characters characters = event.asCharacters();
                        imStatement.setBrand(characters.getData());
                        systemBatchInput.getTransferFile().setBrand(characters.getData());
                      //  writer.add(event);
                        isBrandCode = false;
                    }
                    else if(isAcctNumber){
                        Characters characters = event.asCharacters();
                       imStatement.setAccountNumber(characters.getData());
                        writer.add(event);
                        isAcctNumber = false;

                    }else if(isSeqNo) {
                        Characters characters = event.asCharacters();
                        imStatement.setSeqNo(characters.getData());
                        writer.add(event);
                        isSeqNo = false;
                    }

                    else if (writer != null){
                        writer.add(event);
                    }
                    break;
                default:
                    if (writer != null)
                        writer.add(event);
            }
        }
        imStatement.setInvoiceNumber(invoiceNumber);
        return imStatement;
    }
}
