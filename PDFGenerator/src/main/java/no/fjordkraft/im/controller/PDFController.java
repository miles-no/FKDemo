package no.fjordkraft.im.controller;

import no.fjordkraft.im.exceptions.PDFGeneratorException;
import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.services.PDFGenerator;
import no.fjordkraft.im.util.SetInvoiceASOnline;
import org.eclipse.birt.core.exception.BirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by bhavi on 9/1/2017.
 */

@RestController
public class PDFController {

    private static final Logger logger = LoggerFactory.getLogger(PDFController.class);

    @Autowired
    PDFGenerator pdfGenerator;

    @RequestMapping(value = "/pdf/{statementId}", method = RequestMethod.POST)
    public void generateInvoicePdf(@PathVariable("statementId") Long statementId){
        pdfGenerator.processStatement(statementId);
    }

    @RequestMapping(value = "/pdf", method = RequestMethod.POST)
    public void generateInvoicePdf(@RequestBody List<Long> statementIdList){

        for(Long statementId : statementIdList) {
            logger.debug("Queuing for PDF generation "+ statementId);
            pdfGenerator.processStatement(statementId);
        }

    }

    @RequestMapping(value = "/api/layout/preview", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getDesignPreview(@RequestParam("layoutId") Long layoutId,
                                                   @RequestParam("version") Integer version) throws IOException, BirtException {
        byte[] pdf = pdfGenerator.generatePreview(layoutId, version);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(pdf.length);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=sample.pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/pdf/Statement/generateFile", method = RequestMethod.POST)
    public  byte[] processSingleStatement(@RequestParam("processFilePath") String processFilePath,@RequestParam("brand")String brand,@RequestParam("layoutID")String layoutId ) throws Exception{
        try {
                logger.debug("In PDF Controller for generating PDF for processed file " + processFilePath);
                InputStream inputStream = new FileInputStream(processFilePath);
                XMLInputFactory factory = XMLInputFactory.newInstance();
                XMLEventReader eventReader = factory.createXMLEventReader(inputStream);
                Statement statement =  new Statement();

                Map<String,String> extractedValue = splitFileAndGetFirstStatement(eventReader);
                if(extractedValue!=null && !extractedValue.isEmpty()) {
                      statement.setAccountNumber(extractedValue.get("AccountNumber"));
                      statement.setInvoiceNumber( extractedValue.get("InvoiceNumber"));
                  }
                statement.setOnline(Boolean.TRUE);
                statement.setLayoutID(Long.valueOf(layoutId));
                statement.setBrand(brand);
                statement.setFileName(processFilePath);
                SetInvoiceASOnline.set(true);
                logger.debug("Generating PDF for Online invoice  " + statement.getInvoiceNumber());

                pdfGenerator.generateInvoicePDFSingleStatement(statement);
                byte[] generatedPDF = statement.getGeneratedPDF();

                SetInvoiceASOnline.unset();
                return generatedPDF;
              }
              catch (PDFGeneratorException e) {

                SetInvoiceASOnline.unset();
                logger.error("Error in PDFController while processing online file " + processFilePath + e);
                throw e;
              }
                catch (Exception e) {

            SetInvoiceASOnline.unset();
            logger.error("Error in PDFController while processing online file " + processFilePath + e);
             throw e;
        }
    }

    private Map<String,String> splitFileAndGetFirstStatement(XMLEventReader eventReader) throws XMLStreamException {

        Statement imStatement;
        XMLEventWriter writer = null;
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        boolean isStatementOcr = false;
        boolean isSequenceNumber = false;
        boolean isAccountNumber = false;
        String accountNo = null;
        String invoiceNumber = null;
        boolean isStopProcessing = false;

        Map<String,String> extractedValue = new HashMap<String,String>();
        StringWriter stringOut = null;
        while (eventReader.hasNext() && !isStopProcessing) {
            XMLEvent event = eventReader.nextEvent();
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    //stringOut = new StringWriter();
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();
                    if (("Statement").equalsIgnoreCase(qName)) {
                     /*   stringOut = new StringWriter();
                        writer = outputFactory
                                .createXMLEventWriter(stringOut);
                        writer.add(event);*/
                    } else if (qName.equalsIgnoreCase("StatementOcrNumber")) {
                        isStatementOcr = true;
                      //  writer.add(event);
                    } else if (qName.equalsIgnoreCase("SequenceNumber") ) {
                        isSequenceNumber = true;
                    }else if(qName.equalsIgnoreCase("AccountNumber")) {
                        isAccountNumber = true;
                    }

                    else if (writer != null) {
                        writer.add(event);
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();

                    if ("Statement".equalsIgnoreCase(endElement.getName().getLocalPart())) {
                       /* writer.add(event);
                        writer.flush();
                        writer.close();
                        writer = null;
                        stringOut.getBuffer().setLength(0);*/
                        isStopProcessing = true;
                        break;
                    } else if("SequenceNumber".equalsIgnoreCase(endElement.getName().getLocalPart())) {
                          isSequenceNumber = false;
                         // accountNo = null;
                    }
                    else if( "AccountNumber".equalsIgnoreCase(endElement.getName().getLocalPart())) {
                        isAccountNumber = false;
                    }
                    else if (writer != null) {
                        writer.add(event);
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                   if(isAccountNumber) {
                        Characters characters = event.asCharacters();
                        accountNo = characters.getData();
                        isAccountNumber = false;
                    }  else if(isSequenceNumber){
                       Characters characters = event.asCharacters();
                        invoiceNumber =accountNo+characters.getData();

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
        extractedValue.put("AccountNumber",accountNo);
        extractedValue.put("InvoiceNumber",invoiceNumber);
        return extractedValue;
    }
}
