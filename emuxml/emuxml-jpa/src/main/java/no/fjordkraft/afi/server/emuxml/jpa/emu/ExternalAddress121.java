package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class ExternalAddress121 extends BaseEmuParser implements EmuParser {

    public static final String EXTERNALADDRESS_121 = "ExternalAddress-121";

    String ExternalAddressId;
    String Description;
    String Address;
    String Address2;
    String PostalCode;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setExternalAddressId(getField("ExternalAddressId", streamReader, EXTERNALADDRESS_121));
        setDescription(getField("Description", streamReader, EXTERNALADDRESS_121));
        setAddress(getField("Address", streamReader, EXTERNALADDRESS_121));
        setAddress2(getField("Address2", streamReader, EXTERNALADDRESS_121));
        setPostalCode(getField("PostalCode", streamReader, EXTERNALADDRESS_121));
        checkEnded(EXTERNALADDRESS_121, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(EXTERNALADDRESS_121);
        writeCDataEx(writer, "ExternalAddressId", getExternalAddressId());
        writeCDataEx(writer, "Description", getDescription());
        writeCDataEx(writer, "Address", getAddress());
        writeCDataEx(writer, "Address2", getAddress2());
        writeCDataEx(writer, "PostalCode", getPostalCode());
        writer.writeEndElement();
    }

}