package no.fjordkraft.im.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavi on 6/23/2017.
 */
public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);
    //boolean isEMUXml;
    public static void parse(Reader reader,String basePath,String fileName) throws Exception {
        Boolean isEMUXml = Boolean.FALSE;
        getAvailableMemory();
        try {
            if(fileName.indexOf(".")!=-1){
                fileName = fileName.substring(0,fileName.indexOf("."));
            }

            logger.debug("File to parse is "+fileName);
            long startTime = System.currentTimeMillis();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(reader);
            XMLEventWriter writer = null;
            int i = 1;
            boolean statementOcr = false;
            String statementOcrNumber = "";
            String filePath = "";
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        if (qName.equalsIgnoreCase("Statement")) {
                            filePath = basePath+fileName+"_StatementFile_" + i++ + ".xml";
                            logger.debug("File path to save parsed file "+filePath);
                            writer = outputFactory
                                    .createXMLEventWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.ISO_8859_1) );

                            writer.add(event);
                        } else if (qName.equalsIgnoreCase("StatementOcrNumber")) {
                            statementOcr = true;
                            writer.add(event);
                        } else if (qName.equalsIgnoreCase("Attachments")) {
                            writer.add(event);
                            handleAttachments(eventReader, statementOcrNumber, writer,basePath,isEMUXml);
                            break;
                        } else if (writer != null) {
                            writer.add(event);
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        if (endElement.getName().getLocalPart().equalsIgnoreCase("Statement")) {
                            System.out.println("Statement ended "+ i);
                            writer.add(event);
                            writer.flush();
                            writer.close();
                            writer = null;
                        } else if (writer != null) {
                            writer.add(event);
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if(event.asCharacters().isCData()){
                            System.out.println("Cadta");
                        }
                        if (statementOcr) {
                            Characters characters = event.asCharacters();
                            statementOcrNumber = characters.getData();
                            statementOcr = false;
                        }
                        // break;
                    default:

                        if (writer != null)
                            writer.add(event);

                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Time to split xml file is  " + (endTime - startTime)/1000 + " seconds ");

        } catch (Exception e ) {
            e.printStackTrace();
            throw e;
        }
        getAvailableMemory();
    }

    public static void handleAttachments(XMLEventReader eventReader, String statementOcr,
                                         XMLEventWriter statementWriter, String basePath, Boolean isEMUXml) throws XMLStreamException, IOException {


        XMLEventWriter writer = null;
        int i = 0;
        boolean isAttachment = false;
        int attachmentNo = 1;
        XMLEvent attachmentEvent = null;
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();
                    if (qName.equalsIgnoreCase("Attachment")) {
                        isAttachment = true;
                        attachmentEvent = event;
                    } else if (writer != null) {
                        // writer.add(event);
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();
                    if (isEMUXml && endElement.getName().getLocalPart().equalsIgnoreCase("Attachment")) {
                        isEMUXml = false;
                        statementWriter.add(event);
                        //return;
                    }
                    else if (endElement.getName().getLocalPart().equalsIgnoreCase("Attachments")) {
                        statementWriter.add(event);
                        statementWriter.flush();
                        return;
                    } else if (writer != null) {
                        // writer.add(event);
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isAttachment) {
                        isAttachment = false;
                        Characters characters = event.asCharacters();
                        handleAttachmentData(characters.getData(), statementWriter,statementOcr, attachmentNo++,attachmentEvent,basePath,isEMUXml);
                        //isEMUXml = false;
                        logger.debug("one attachment handled " + statementOcr);
                    }

                    break;
                default:

            }
        }
    }

    static void handleAttachmentData(String data, XMLEventWriter statementWriter,String statementOcr, int attachmentNo,
                                     XMLEvent attachmentEvent,String basePath,Boolean isEMUXml) throws XMLStreamException, IOException{

        XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
        data = data.replaceAll("&lt;!\\[CDATA\\[", "");
        data = data.replaceAll("\\]\\]&gt;","" );
        XMLEventReader eventReader = xmlFactory.createXMLEventReader(new ByteArrayInputStream(data.getBytes()));
        boolean isPdf = false;

        FileOutputStream fos =  null;// new FileOutputStream(new File("test_"+attachmentNo+".pdf"));
        StringBuilder pdfData = new StringBuilder();
        List<XMLEvent> attachmentXMLEvents = new ArrayList<XMLEvent>();
        boolean addAttachmentXMLEvents = true;
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();
				/*if (qName.equalsIgnoreCase("VEDLEGG_FORMAT")) {
					isFormatTag = true;
				}else */if (qName.equalsIgnoreCase("VEDLEGG_PDF")) {
                    System.out.println("pdf ");
                    isPdf = true;
                    addAttachmentXMLEvents = false;
                    attachmentXMLEvents = null;
                }else if(qName.equalsIgnoreCase("VEDLEGG_EMUXML")){
                    System.out.println("Emuxml ");
                    isEMUXml = true;
                    addAttachmentXMLEvents = false;
                    statementWriter.add(attachmentEvent);
                    for(XMLEvent xmlEvent : attachmentXMLEvents){
                        statementWriter.add(xmlEvent);
                    }
                    statementWriter.add(event);
                }else if(isEMUXml){
                    statementWriter.add(event);
                }

                    if(addAttachmentXMLEvents)
                        attachmentXMLEvents.add(event);
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();
                    if (isPdf && endElement.getName().getLocalPart().equalsIgnoreCase("VEDLEGG_PDF")) {
                        fos = new FileOutputStream(new File(basePath+statementOcr+"_"+attachmentNo+".pdf"));
                        byte[] decoded = Base64.decodeBase64(pdfData.toString().getBytes());
                        fos.write(decoded);
                        fos.close();
                        isPdf = false;
                        //return;
                    }
                    else if(addAttachmentXMLEvents){
                        attachmentXMLEvents.add(event);
                    } else if("VEDLEGG_E2B".equals(endElement.getName().getLocalPart())){
                        statementWriter.add(event);
                    }else if(isEMUXml){
                        statementWriter.add(event);
                    }
                    break;


                case XMLStreamConstants.CHARACTERS :
                    if(isPdf){
                        Characters characters = event.asCharacters();
                        pdfData.append(characters.getData());

                        //byte[] decoded = Base64.decodeBase64(characters.getData().getBytes());
                        //fos.write(decoded);
                        //handleAttachmentData(characters.getData(),  XMLEventWriter writer);
                    }else if(addAttachmentXMLEvents){
                        attachmentXMLEvents.add(event);
                    }else if(isEMUXml){
                        statementWriter.add(event);
                    }

                    break;
                default:
				/*if (writer != null)
					writer.add(event);

			}*/
                    break;
            }
        }

    }

    public static long getAvailableMemory() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory(); // current heap allocated to
        // the VM process
        System.out.println("Total memomry = " + totalMemory);
        long freeMemory = runtime.freeMemory(); // out of the current heap, how
        // much is free
        long maxMemory = runtime.maxMemory(); // Max heap VM can use e.g. Xmx
        // setting
        long usedMemory = totalMemory - freeMemory; // how much of the current
        // heap the VM is using
        long availableMemory = maxMemory - usedMemory; // available memory i.e.
        // Maximum heap size
        // minus the current
        // amount used
        System.out.println("Total memomry = " + totalMemory/(1024*1024) + "mb Free memory " + freeMemory/(1024*1024) + "mb max memory " + maxMemory/(1024*1024)
                + "mb used memory " + usedMemory/(1024*1024) + "mb available memory " + availableMemory/(1024*1024) + " mb");
        return availableMemory;
    }
}
