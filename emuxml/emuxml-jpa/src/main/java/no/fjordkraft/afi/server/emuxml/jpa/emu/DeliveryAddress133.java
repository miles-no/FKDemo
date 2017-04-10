package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class DeliveryAddress133 extends BaseEmuParser implements EmuParser {

    public static final String DELIVERYADDRESS_133 = "DeliveryAddress-133";

    String CounterNo;
    String Address;
    String PostalCode;
    String City;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setCounterNo(getField("CounterNo", streamReader, DELIVERYADDRESS_133));
        setAddress(getField("Address", streamReader, DELIVERYADDRESS_133));
        setPostalCode(getField("PostalCode", streamReader, DELIVERYADDRESS_133));
        setCity(getField("City", streamReader, DELIVERYADDRESS_133));
        checkEnded(DELIVERYADDRESS_133, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(DELIVERYADDRESS_133);
        write(writer, "CounterNo", getCounterNo());
        writeCDataEx(writer, "Address", getAddress());
        writeCDataEx(writer, "PostalCode", getPostalCode());
        writeCDataEx(writer, "City", getCity());
        writer.writeEndElement();
    }

}