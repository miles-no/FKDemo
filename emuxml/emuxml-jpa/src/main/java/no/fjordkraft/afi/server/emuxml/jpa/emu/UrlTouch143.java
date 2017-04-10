package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class UrlTouch143 extends BaseEmuParser implements EmuParser {

    public static final String URL_TOUCH_143 = "Url_touch-143";

    String Url;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setUrl(getField("Url", streamReader, URL_TOUCH_143));
        checkEnded(URL_TOUCH_143, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer)
            throws XMLStreamException {
        writer.writeStartElement(URL_TOUCH_143);
        write(writer, "Url", getUrl());
        writer.writeEndElement();
    }

}