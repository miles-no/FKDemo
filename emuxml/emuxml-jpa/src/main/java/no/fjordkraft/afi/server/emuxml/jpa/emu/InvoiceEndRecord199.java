package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class InvoiceEndRecord199 extends BaseEmuParser implements EmuParser {

    public static final String INVOICEENDRECORD_199 = "InvoiceEndRecord-199";

    String TotalLines;
    String InvoiceNo;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setTotalLines(getField("TotalLines", streamReader, INVOICEENDRECORD_199));
        setInvoiceNo(getField("InvoiceNo", streamReader, INVOICEENDRECORD_199));
        checkEnded(INVOICEENDRECORD_199, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(INVOICEENDRECORD_199);
        write(writer, "TotalLines", getTotalLines());
        write(writer, "InvoiceNo", getInvoiceNo());
        writer.writeEndElement();
    }
}