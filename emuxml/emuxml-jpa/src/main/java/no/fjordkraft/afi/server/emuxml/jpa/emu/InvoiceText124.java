package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class InvoiceText124 extends BaseEmuParser implements EmuParser {

    public static final String INVOICETEXT_124 = "InvoiceText-124";

    String InvoiceText;
    String ServiceAgreeSerialNo;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setInvoiceText(getField("InvoiceText", streamReader, INVOICETEXT_124));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, INVOICETEXT_124));
        checkEnded(INVOICETEXT_124, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(INVOICETEXT_124);
        write(writer, "InvoiceText", getInvoiceText());
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        writer.writeEndElement();
    }

}