package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class ReadingInfo111 extends BaseEmuParser implements EmuParser {

    public static final String READINGINFO_111 = "ReadingInfo-111";

    String StartDate;
    String EndDate;
    String MeterId;
    String StartReading;
    String FinalC_Reading;
    String AreaFactor;
    String FixedPricePeriodStart;
    String FixedPricePeriodEnd;
    String TotalDigits;
    String TotalDecimals;
    String Method;
    String MethodStartReading;
    String MethodFinalC_Reading;
    String V_Reading;
    String ProdCounterNo;
    String MeterCounterNo;
    String Denomination;
    String TotalDays;
    String Description;
    String FixedPricePeriodDays;
    String ServiceAgreeSerialNo;
    String TotConsCurrCounter;
    String MeterIdNew;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setStartDate(getField("StartDate", streamReader, READINGINFO_111));
        setEndDate(getField("EndDate", streamReader, READINGINFO_111));
        setMeterId(getField("MeterId", streamReader, READINGINFO_111));
        setStartReading(getField("StartReading", streamReader, READINGINFO_111));
        setFinalC_Reading(getField("FinalC-Reading", streamReader, READINGINFO_111));
        setAreaFactor(getField("AreaFactor", streamReader, READINGINFO_111));
        setFixedPricePeriodStart(getField("FixedPricePeriodStart", streamReader, READINGINFO_111));
        setFixedPricePeriodEnd(getField("FixedPricePeriodEnd", streamReader, READINGINFO_111));
        setTotalDigits(getField("TotalDigits", streamReader, READINGINFO_111));
        setTotalDecimals(getField("TotalDecimals", streamReader, READINGINFO_111));
        setMethod(getField("Method", streamReader, READINGINFO_111));
        setMethodStartReading(getField("MethodStartReading", streamReader, READINGINFO_111));
        setMethodFinalC_Reading(getField("MethodFinalC-Reading", streamReader, READINGINFO_111));
        setV_Reading(getField("V-Reading", streamReader, READINGINFO_111));
        setProdCounterNo(getField("ProdCounterNo", streamReader, READINGINFO_111));
        setMeterCounterNo(getField("MeterCounterNo", streamReader, READINGINFO_111));
        setDenomination(getField("Denomination", streamReader, READINGINFO_111));
        setTotalDays(getField("TotalDays", streamReader, READINGINFO_111));
        setDescription(getField("Description", streamReader, READINGINFO_111));
        setFixedPricePeriodDays(getField("FixedPricePeriodDays", streamReader, READINGINFO_111));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, READINGINFO_111));
        setTotConsCurrCounter(getField("TotConsCurrCounter", streamReader, READINGINFO_111));
        setMeterIdNew(getField("MeterIdNew", streamReader, READINGINFO_111));
        checkEnded(READINGINFO_111, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(READINGINFO_111);
        write(writer, "StartDate", formatDate(getStartDate()));
        write(writer, "EndDate", formatDate(getEndDate()));
        writeCDataEx(writer, "MeterId", getMeterId());
        write(writer, "StartReading", getStartReading());
        write(writer, "FinalC-Reading", getFinalC_Reading());
        write(writer, "AreaFactor", getAreaFactor());
        if (StringUtils.isNotEmpty(getFixedPricePeriodStart())) {
            write(writer, "FixedPricePeriodStart", formatDate(getFixedPricePeriodStart()));
        }
        if (StringUtils.isNotEmpty(getFixedPricePeriodEnd())) {
            write(writer, "FixedPricePeriodEnd", formatDate(getFixedPricePeriodEnd()));
        }
        write(writer, "TotalDigits", getTotalDigits());
        write(writer, "TotalDecimals", getTotalDecimals());
        writeCDataEx(writer, "Method", getMethod());
        writeCDataEx(writer, "MethodStartReading", getMethodStartReading());
        write(writer, "MethodFinalC-Reading", getMethodFinalC_Reading());
        writeCDataEx(writer, "V-Reading", getV_Reading());
        writeCDataEx(writer, "ProdCounterNo", getProdCounterNo());
        write(writer, "MeterCounterNo", getMeterCounterNo());
        writeCDataEx(writer, "Denomination", getDenomination());
        write(writer, "TotalDays", getTotalDays());
        writeCDataEx(writer, "Description", getDescription());
        write(writer, "FixedPricePeriodDays", getFixedPricePeriodDays());
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        write(writer, "TotConsCurrCounter", getTotConsCurrCounter());
        writeCDataEx(writer, "MeterIdNew", getMeterIdNew());
        writer.writeEndElement();
    }
}