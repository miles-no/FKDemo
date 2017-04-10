package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class Attachment144 extends BaseEmuParser implements EmuParser {

    public static final String ATTACHMENT_144 = "Attachment-144";

    String Attachment;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setAttachment(getField("Attachment", streamReader, ATTACHMENT_144));
        checkEnded(ATTACHMENT_144, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer)
            throws XMLStreamException {
        writer.writeStartElement(ATTACHMENT_144);
        write(writer, "Attachment", getAttachment());
        writer.writeEndElement();
    }

}