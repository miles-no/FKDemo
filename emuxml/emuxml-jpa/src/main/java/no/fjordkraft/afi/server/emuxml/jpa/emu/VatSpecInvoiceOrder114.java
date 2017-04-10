package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class VatSpecInvoiceOrder114 extends BaseEmuParser implements EmuParser {

    public static final String VATSPECINVOICEORDER_114 = "VatSpecInvoiceOrder-114";

    String VatRate;
    String SumBasis;
    String SumVat;
    String ServiceAgreeSerialNo;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setVatRate(getField("VatRate", streamReader, VATSPECINVOICEORDER_114));
        setSumBasis(getField("SumBasis", streamReader, VATSPECINVOICEORDER_114));
        setSumVat(getField("SumVat", streamReader, VATSPECINVOICEORDER_114));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, VATSPECINVOICEORDER_114));
        checkEnded(VATSPECINVOICEORDER_114, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(VATSPECINVOICEORDER_114);
        write(writer, "VatRate", getVatRate());
        write(writer, "SumBasis", getSumBasis());
        write(writer, "SumVat", getSumVat());
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        writer.writeEndElement();
    }
}