package no.fjordkraft.im.services.impl;

/**
 * Created by miles on 5/2/2017.
 */

import no.fjordkraft.im.exceptions.StatementSplitterException;
import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.services.StatementSplitter;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;

@Service
public class StatementSplitterImpl implements StatementSplitter {

    private static final Logger logger = LoggerFactory.getLogger(StatementSplitterImpl.class);

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    StatementService statementService;

    @Autowired
    SystemBatchInputServiceImpl systemBatchInputService;

    @Override
    public void batchFileSplit(InputStream inputStream, String filename, SystemBatchInput systemBatchInput) throws Exception {

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(inputStream);
        String numberOfRecords = splitAndSaveInDB(eventReader, systemBatchInput);
        statementService.updateStatementsBySiId(systemBatchInput.getId(), StatementStatusEnum.PENDING);
        systemBatchInput.setNumOfRecords(Integer.valueOf(numberOfRecords));
    }

    @Override
    public void batchFileSplit(Reader inputStream, String filename, SystemBatchInput systemBatchInput) throws Exception {

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(inputStream);
        String numberOfRecords = splitAndSaveInDB(eventReader, systemBatchInput);
        systemBatchInput.setNumOfRecords(Integer.valueOf(numberOfRecords));

    }

    private String splitAndSaveInDB(XMLEventReader eventReader, SystemBatchInput systemBatchInput) throws Exception {
        try {
            Statement imStatement;
            XMLEventWriter writer = null;
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            boolean isStatementOcr = false;
            String numberOfRecords = null;
            boolean isNumberOfRecords = false;

            imStatement = new Statement();
            imStatement.setSystemBatchInput(systemBatchInput);
            StringWriter stringOut = null;
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        //stringOut = new StringWriter();
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
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
                        }else if (writer != null) {
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
                            //logger.debug("saving statement with ocr"+imStatement.getStatementId());
                            statementService.saveIMStatementinDB(stringOut.toString(), imStatement);
                            stringOut.getBuffer().setLength(0);
                            //stringOut = null;
                            imStatement = new Statement();
                            imStatement.setSystemBatchInput(systemBatchInput);
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
                        } else if (isNumberOfRecords) {
                            Characters characters = event.asCharacters();
                            numberOfRecords = characters.getData();
                            isNumberOfRecords = false;
                        }else if (writer != null){
                            writer.add(event);
                        }
                        break;
                    default:
                        if (writer != null)
                            writer.add(event);
                }
            }
            return numberOfRecords;
        } catch (Exception e) {
            //logger.error("Exception while splitting the file "+systemBatchInput.getId() + " "+ systemBatchInput.getTransferFile().getFilename(),e);
            //throw e;

          /*  statementService.deleteStatementBySiId(systemBatchInput.getId());*/

            throw new StatementSplitterException(e.getMessage());
        } finally {

        }
    }
}
