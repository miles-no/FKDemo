package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class UnpaidInvoiceInfo104 extends BaseEmuParser implements EmuParser {

    public static final String UNPAIDINVOICEINFO_104 = "UnpaidInvoiceInfo-104";

    String InvoiceNo;
    String DueDate;
    String InitialAmount;
    String AlreadyPaid;
    String TotalPrinted;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setInvoiceNo(getField("InvoiceNo", streamReader, UNPAIDINVOICEINFO_104));
        setInvoiceNo(getField("DueDate", streamReader, UNPAIDINVOICEINFO_104));
        setInvoiceNo(getField("InitialAmount", streamReader, UNPAIDINVOICEINFO_104));
        setInvoiceNo(getField("AlreadyPaid", streamReader, UNPAIDINVOICEINFO_104));
        setInvoiceNo(getField("TotalPrinted", streamReader, UNPAIDINVOICEINFO_104));
        checkEnded(UNPAIDINVOICEINFO_104, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(UNPAIDINVOICEINFO_104);
        write(writer, "InvoiceNo", getInvoiceNo());
        write(writer, "DueDate", formatDate(getInvoiceNo()));
        write(writer, "InitialAmount", getInvoiceNo());
        write(writer, "AlreadyPaid", getInvoiceNo());
        write(writer, "TotalPrinted", getInvoiceNo());
        writer.writeEndElement();
    }

}