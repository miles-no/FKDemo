package no.fjordkraft.im.services.impl;

/**
 * Created by miles on 5/2/2017.
 */

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.model.SystemBatchInput;
import no.fjordkraft.im.repository.StatementRepository;
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

    @Override
    public void batchFileSplit(InputStream inputStream, String filename, SystemBatchInput systemBatchInput) throws XMLStreamException, IOException {

        StopWatch stopWatch = new StopWatch("StatementSplitterImpl::batchFileSplit");
        stopWatch.start();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(inputStream);
        splitAndSaveInDB(eventReader, systemBatchInput);
        //long endTime = System.currentTimeMillis();
        stopWatch.stop();
        logger.debug(stopWatch.prettyPrint());
    }

    private void splitAndSaveInDB(XMLEventReader eventReader, SystemBatchInput systemBatchInput) throws IOException {
        try {
            Statement imStatement;
            XMLEventWriter writer = null;
            //XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            //int i = 1;
            boolean isStatementOcr = false;
            //String brandCode = "";
            String tempFilePath = "src/main/resources/statementTargetFile.tmp";
            imStatement = new Statement();
            imStatement.setSystemBatchInput(systemBatchInput);
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        if (("Statement").equalsIgnoreCase(qName)) {
                            writer = outputFactory
                                    .createXMLEventWriter(new FileOutputStream(tempFilePath));
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
                            saveIMStatementinDB(new File(tempFilePath), imStatement);
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    void saveIMStatementinDB(File statementFile, Statement imStatement) throws IOException {
        //Statement statement = new Statement();
        String xml = FileUtils.readFileToString(statementFile, StandardCharsets.ISO_8859_1);

        //statement.setSiId(imStatement.getSiId());
        //statement.setStatementId(imStatement.getStatementId());
        imStatement.setPayload(xml);
        imStatement.setStatus(StatementStatusEnum.PENDING.getStatus());
        imStatement.setCreateTime(new Timestamp(System.currentTimeMillis()));
        imStatement.setUdateTime(new Timestamp(System.currentTimeMillis()));
        statementRepository.save(imStatement);
    }
}
