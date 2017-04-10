package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class VatSpecInvoice142 extends BaseEmuParser implements EmuParser {

    public static final String VATSPECINVOICE_142 = "VatSpecInvoice-142";

    String VatRate;
    String SumBasis;
    String SumVat;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setVatRate(getField("VatRate", streamReader, VATSPECINVOICE_142));
        setSumBasis(getField("SumBasis", streamReader, VATSPECINVOICE_142));
        setSumVat(getField("SumVat", streamReader, VATSPECINVOICE_142));
        checkEnded(VATSPECINVOICE_142, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(VATSPECINVOICE_142);
        write(writer, "VatRate", getVatRate());
        write(writer, "SumBasis", getSumBasis());
        write(writer, "SumVat", getSumVat());
        writer.writeEndElement();
    }
}