package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class MoraSpecification122 extends BaseEmuParser implements EmuParser {

    public static final String MORASPECIFICATION_122 = "MoraSpecification-122";

    String InvoiceNo;
    String SrlNo;
    String StartDate;
    String EndDate;
    String Basis;
    String NoOfDays;
    String MoraRate;
    String MoraInterest;
    String ServiceAgreeSerialNo;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setInvoiceNo(getField("InvoiceNo", streamReader, MORASPECIFICATION_122));
        setSrlNo(getField("SrlNo", streamReader, MORASPECIFICATION_122));
        setStartDate(getField("StartDate", streamReader, MORASPECIFICATION_122));
        setEndDate(getField("EndDate", streamReader, MORASPECIFICATION_122));
        setBasis(getField("Basis", streamReader, MORASPECIFICATION_122));
        setNoOfDays(getField("NoOfDays", streamReader, MORASPECIFICATION_122));
        setMoraRate(getField("MoraRate", streamReader, MORASPECIFICATION_122));
        setMoraInterest(getField("MoraInterest", streamReader, MORASPECIFICATION_122));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, MORASPECIFICATION_122));
        checkEnded(MORASPECIFICATION_122, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer)
            throws XMLStreamException {
        if (StringUtils.isNotEmpty(getInvoiceNo())) {
            writer.writeStartElement(MORASPECIFICATION_122);
            write(writer, "InvoiceNo", getInvoiceNo());
            write(writer, "SrlNo", getSrlNo());
            write(writer, "StartDate", getStartDate());
            write(writer, "EndDate", getEndDate());
            write(writer, "Basis", getBasis());
            write(writer, "NoOfDays", getNoOfDays());
            write(writer, "MoraRate", getMoraRate());
            write(writer, "MoraInterest", getMoraInterest());
            write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
            writer.writeEndElement();
        }
    }
}
