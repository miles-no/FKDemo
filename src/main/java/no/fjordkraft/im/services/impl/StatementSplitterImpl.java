package no.fjordkraft.im.services.impl;

/**
 * Created by miles on 5/2/2017.
 */

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.services.StatementSplitter;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

@Service
public class StatementSplitterImpl implements StatementSplitter {

    private static final Logger logger = LoggerFactory.getLogger(StatementSplitterImpl.class);

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    StatementService statementService;

    @Override
    public void batchFileSplit(InputStream inputStream, String filename, SystemBatchInput systemBatchInput) throws Exception {

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(inputStream);
        splitAndSaveInDB(eventReader, systemBatchInput);

    }

    private void splitAndSaveInDB(XMLEventReader eventReader, SystemBatchInput systemBatchInput) throws Exception {
        String tempFilePath = "src/main/resources/"+systemBatchInput.getId()+".xml";
        try {
            Statement imStatement;
            XMLEventWriter writer = null;
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            boolean isStatementOcr = false;

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
                        } else if (writer != null) {
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
                        } else if (writer != null){
                            writer.add(event);
                        }
                        break;
                    default:
                        if (writer != null)
                            writer.add(event);
                }
            }
        } catch (Exception e) {
            logger.debug("Exception while splitting the file "+systemBatchInput.getId() + " "+ systemBatchInput.getFilename(),e);
            throw e;
        } finally {
            File f = new File(tempFilePath);
            if(f !=null && f.exists()){
                logger.debug("deleting file with name "+ f.getAbsolutePath());
                f.delete();
            }
        }
    }
}
