package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class PassThroughInvoiceInfo109 extends BaseEmuParser implements EmuParser {

    public static final String PASS_THROUGHINVOICEINFO_109 = "Pass-ThroughInvoiceInfo-109";

    String FirstInvoice;
    String ExtCustRef;
    String ToDate;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setFirstInvoice(getField("FirstInvoice", streamReader, PASS_THROUGHINVOICEINFO_109));
        setExtCustRef(getField("ExtCustRef", streamReader, PASS_THROUGHINVOICEINFO_109));
        setToDate(getField("ToDate", streamReader, PASS_THROUGHINVOICEINFO_109));
        checkEnded(PASS_THROUGHINVOICEINFO_109, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(PASS_THROUGHINVOICEINFO_109);
        write(writer, "FirstInvoice", getFirstInvoice());
        write(writer, "ExtCustRef", getExtCustRef());
        writeCDataEx(writer, "ToDate", getToDate());
        writer.writeEndElement();
    }
}
