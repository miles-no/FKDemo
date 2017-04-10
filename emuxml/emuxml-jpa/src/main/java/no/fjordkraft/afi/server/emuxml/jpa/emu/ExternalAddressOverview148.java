package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class ExternalAddressOverview148 extends BaseEmuParser implements EmuParser {

    public static final String EXTERNALADDRESSOVERVIEW_148 = "ExternalAddressOverview-148";

    String ExternalAddressId;
    String Net;
    String Gross;
    String Vat;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setExternalAddressId(getField("ExternalAddressId", streamReader, EXTERNALADDRESSOVERVIEW_148));
        setNet(getField("Net", streamReader, EXTERNALADDRESSOVERVIEW_148));
        setGross(getField("Gross", streamReader, EXTERNALADDRESSOVERVIEW_148));
        setVat(getField("Vat", streamReader, EXTERNALADDRESSOVERVIEW_148));
        checkEnded(EXTERNALADDRESSOVERVIEW_148, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer)
            throws XMLStreamException {
        writer.writeStartElement(EXTERNALADDRESSOVERVIEW_148);
        write(writer, "ExternalAddressId", getExternalAddressId());
        write(writer, "Net", getNet());
        write(writer, "Gross", getGross());
        write(writer, "Vat", getVat());
        writer.writeEndElement();
    }

}