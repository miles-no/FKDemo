package no.fjordkraft.afi.server.emuxml.jpa.emu;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public interface EmuParser {

    void parse(XMLStreamReader streamReader) throws XMLStreamException;

    void writeXml(XMLStreamWriter writer) throws XMLStreamException;
}