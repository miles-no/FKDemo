package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class DunningCharge146 extends BaseEmuParser implements EmuParser {

    public static final String DUNNINGCHARGE_146 = "DunningCharge-146";

    String Text;
    String Gross;
    String DunnningInvoice;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setText(getField("Text", streamReader, DUNNINGCHARGE_146));
        setGross(getField("Gross", streamReader, DUNNINGCHARGE_146));
        setDunnningInvoice(getField("DunnningInvoice", streamReader, DUNNINGCHARGE_146));
        checkEnded(DUNNINGCHARGE_146, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer)
            throws XMLStreamException {
        writer.writeStartElement(DUNNINGCHARGE_146);
        write(writer, "Text", getText());
        write(writer, "Gross", getGross());
        write(writer, "DunnningInvoice", getDunnningInvoice());
        writer.writeEndElement();
    }
}