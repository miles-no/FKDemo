package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class PriceInfo116 extends BaseEmuParser implements EmuParser {

    public static final String PRICEINFO_116 = "PriceInfo-116";

    String StatisticCode;
    String Gross;
    String PriceCode;
    String Description;
    String Designation;
    String PriceDate;
    String Gross_high_precision;
    String ServiceAgreeSerialNo;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setStatisticCode(getField("StatisticCode", streamReader, PRICEINFO_116));
        setGross(getField("Gross", streamReader, PRICEINFO_116));
        setPriceCode(getField("PriceCode", streamReader, PRICEINFO_116));
        setDescription(getField("Description", streamReader, PRICEINFO_116));
        setDesignation(getField("Designation", streamReader, PRICEINFO_116));
        setPriceDate(getField("PriceDate", streamReader, PRICEINFO_116));
        setGross_high_precision(getField("Gross_high_precision", streamReader, PRICEINFO_116));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, PRICEINFO_116));
        checkEnded(PRICEINFO_116, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(PRICEINFO_116);
        writeCDataEx(writer, "StatisticCode", getStatisticCode());
        write(writer, "Gross", getGross());
        writeCDataEx(writer, "PriceCode", getPriceCode());
        writeCDataEx(writer, "Description", getDescription());
        writeCDataEx(writer, "Designation", getDesignation());
        write(writer, "PriceDate", formatDate(getPriceDate()));
        write(writer, "Gross_high_precision", getGross_high_precision());
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        writer.writeEndElement();
    }
}
