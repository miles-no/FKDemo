package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class HistoricalInfo130 extends BaseEmuParser implements EmuParser {

    public static final String HISTORICALINFO_130 = "HistoricalInfo-130";

    String InvoiceNo;
    String StartDate;
    String EndDate;
    String Consumption;
    String ServiceAgreeSerialNo;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setInvoiceNo(getField("InvoiceNo", streamReader, HISTORICALINFO_130));
        setStartDate(getField("StartDate", streamReader, HISTORICALINFO_130));
        setEndDate(getField("EndDate", streamReader, HISTORICALINFO_130));
        setConsumption(getField("Consumption", streamReader, HISTORICALINFO_130));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, HISTORICALINFO_130));
        checkEnded(HISTORICALINFO_130, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(HISTORICALINFO_130);
        write(writer, "InvoiceNo", getInvoiceNo());
        write(writer, "StartDate", getStartDate());
        write(writer, "EndDate", getEndDate());
        write(writer, "Consumption", getConsumption());
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        writer.writeEndElement();
    }

}