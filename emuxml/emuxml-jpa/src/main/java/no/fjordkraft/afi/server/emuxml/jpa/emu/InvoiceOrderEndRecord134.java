package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class InvoiceOrderEndRecord134 extends BaseEmuParser implements EmuParser {

    public static final String INVOICEORDERENDRECORD_134 = "InvoiceOrderEndRecord-134";

    String BillingGroup;
    String ServiceAgreeSerialNo;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setBillingGroup(getField("BillingGroup", streamReader, INVOICEORDERENDRECORD_134));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, INVOICEORDERENDRECORD_134));
        checkEnded(INVOICEORDERENDRECORD_134, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(INVOICEORDERENDRECORD_134);
        writeCDataEx(writer, "BillingGroup", getBillingGroup());
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        writer.writeEndElement();
    }
}
