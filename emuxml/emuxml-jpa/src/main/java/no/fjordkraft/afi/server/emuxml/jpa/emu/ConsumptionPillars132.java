package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class ConsumptionPillars132 extends BaseEmuParser implements EmuParser {

    public static final String CONSUMPTIONPILLARS_132 = "ConsumptionPillars-132";

    String PeriodDescription1;
    String LastYearConsumption1;
    String ThisYearConsumption1;
    String PeriodDescription2;
    String LastYearConsumption2;
    String ThisYearConsumption2;
    String PeriodDescription3;
    String LastYearConsumption3;
    String ThisYearConsumption3;
    String PeriodDescription4;
    String LastYearConsumption4;
    String ThisYearConsumption4;
    String PeriodDescription5;
    String LastYearConsumption5;
    String ThisYearConsumption5;
    String PeriodDescription6;
    String LastYearConsumption6;
    String ThisYearConsumption6;
    String PeriodDescription7;
    String LastYearConsumption7;
    String ThisYearConsumption7;
    String PeriodDescription8;
    String LastYearConsumption8;
    String ThisYearConsumption8;
    String PeriodDescription9;
    String LastYearConsumption9;
    String ThisYearConsumption9;
    String PeriodDescription10;
    String LastYearConsumption10;
    String ThisYearConsumption10;
    String PeriodDescription11;
    String LastYearConsumption11;
    String ThisYearConsumption11;
    String PeriodDescription12;
    String LastYearConsumption12;
    String ThisYearConsumption12;
    String ServiceAgreeSerialNo;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setPeriodDescription1(getField("PeriodDescription1", streamReader, CONSUMPTIONPILLARS_132));
        setLastYearConsumption1(getField("LastYearConsumption1", streamReader, CONSUMPTIONPILLARS_132));
        setThisYearConsumption1(getField("ThisYearConsumption1", streamReader, CONSUMPTIONPILLARS_132));
        setPeriodDescription2(getField("PeriodDescription2", streamReader, CONSUMPTIONPILLARS_132));
        setLastYearConsumption2(getField("LastYearConsumption2", streamReader, CONSUMPTIONPILLARS_132));
        setThisYearConsumption2(getField("ThisYearConsumption2", streamReader, CONSUMPTIONPILLARS_132));
        setPeriodDescription3(getField("PeriodDescription3", streamReader, CONSUMPTIONPILLARS_132));
        setLastYearConsumption3(getField("LastYearConsumption3", streamReader, CONSUMPTIONPILLARS_132));
        setThisYearConsumption3(getField("ThisYearConsumption3", streamReader, CONSUMPTIONPILLARS_132));
        setPeriodDescription4(getField("PeriodDescription4", streamReader, CONSUMPTIONPILLARS_132));
        setLastYearConsumption4(getField("LastYearConsumption4", streamReader, CONSUMPTIONPILLARS_132));
        setThisYearConsumption4(getField("ThisYearConsumption4", streamReader, CONSUMPTIONPILLARS_132));
        setPeriodDescription5(getField("PeriodDescription5", streamReader, CONSUMPTIONPILLARS_132));
        setLastYearConsumption5(getField("LastYearConsumption5", streamReader, CONSUMPTIONPILLARS_132));
        setThisYearConsumption5(getField("ThisYearConsumption5", streamReader, CONSUMPTIONPILLARS_132));
        setPeriodDescription6(getField("PeriodDescription6", streamReader, CONSUMPTIONPILLARS_132));
        setLastYearConsumption6(getField("LastYearConsumption6", streamReader, CONSUMPTIONPILLARS_132));
        setThisYearConsumption6(getField("ThisYearConsumption6", streamReader, CONSUMPTIONPILLARS_132));
        setPeriodDescription7(getField("PeriodDescription7", streamReader, CONSUMPTIONPILLARS_132));
        setLastYearConsumption7(getField("LastYearConsumption7", streamReader, CONSUMPTIONPILLARS_132));
        setThisYearConsumption7(getField("ThisYearConsumption7", streamReader, CONSUMPTIONPILLARS_132));
        setPeriodDescription8(getField("PeriodDescription8", streamReader, CONSUMPTIONPILLARS_132));
        setLastYearConsumption8(getField("LastYearConsumption8", streamReader, CONSUMPTIONPILLARS_132));
        setThisYearConsumption8(getField("ThisYearConsumption8", streamReader, CONSUMPTIONPILLARS_132));
        setPeriodDescription9(getField("PeriodDescription9", streamReader, CONSUMPTIONPILLARS_132));
        setLastYearConsumption9(getField("LastYearConsumption9", streamReader, CONSUMPTIONPILLARS_132));
        setThisYearConsumption9(getField("ThisYearConsumption9", streamReader, CONSUMPTIONPILLARS_132));
        setPeriodDescription10(getField("PeriodDescription10", streamReader, CONSUMPTIONPILLARS_132));
        setLastYearConsumption10(getField("LastYearConsumption10", streamReader, CONSUMPTIONPILLARS_132));
        setThisYearConsumption10(getField("ThisYearConsumption10", streamReader, CONSUMPTIONPILLARS_132));
        setPeriodDescription11(getField("PeriodDescription11", streamReader, CONSUMPTIONPILLARS_132));
        setLastYearConsumption11(getField("LastYearConsumption11", streamReader, CONSUMPTIONPILLARS_132));
        setThisYearConsumption11(getField("ThisYearConsumption11", streamReader, CONSUMPTIONPILLARS_132));
        setPeriodDescription12(getField("PeriodDescription12", streamReader, CONSUMPTIONPILLARS_132));
        setLastYearConsumption12(getField("LastYearConsumption12", streamReader, CONSUMPTIONPILLARS_132));
        setThisYearConsumption12(getField("ThisYearConsumption12", streamReader, CONSUMPTIONPILLARS_132));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, CONSUMPTIONPILLARS_132));
        checkEnded(CONSUMPTIONPILLARS_132, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(CONSUMPTIONPILLARS_132);
        writeCDataEx(writer, "PeriodDescription1", getPeriodDescription1());
        if (getPeriodDescription1() != null && getPeriodDescription1().length() > 0) {
            write(writer, "LastYearConsumption1", getLastYearConsumption1());
            write(writer, "ThisYearConsumption1", getThisYearConsumption1());
        }
        writeCDataEx(writer, "PeriodDescription2", getPeriodDescription2());
        if (getPeriodDescription2() != null && getPeriodDescription2().length() > 0) {
            write(writer, "LastYearConsumption2", getLastYearConsumption2());
            write(writer, "ThisYearConsumption2", getThisYearConsumption2());
        }
        writeCDataEx(writer, "PeriodDescription3", getPeriodDescription3());
        if (getPeriodDescription3() != null && getPeriodDescription3().length() > 0) {
            write(writer, "LastYearConsumption3", getLastYearConsumption3());
            write(writer, "ThisYearConsumption3", getThisYearConsumption3());
        }
        writeCDataEx(writer, "PeriodDescription4", getPeriodDescription4());
        if (getPeriodDescription4() != null && getPeriodDescription4().length() > 0) {
            write(writer, "LastYearConsumption4", getLastYearConsumption4());
            write(writer, "ThisYearConsumption4", getThisYearConsumption4());
        }
        writeCDataEx(writer, "PeriodDescription5", getPeriodDescription5());
        if (getPeriodDescription5() != null && getPeriodDescription5().length() > 0) {
            write(writer, "LastYearConsumption5", getLastYearConsumption5());
            write(writer, "ThisYearConsumption5", getThisYearConsumption5());
        }
        writeCDataEx(writer, "PeriodDescription6", getPeriodDescription6());
        if (getPeriodDescription6() != null && getPeriodDescription6().length() > 0) {
            write(writer, "LastYearConsumption6", getLastYearConsumption6());
            write(writer, "ThisYearConsumption6", getThisYearConsumption6());
        }
        writeCDataEx(writer, "PeriodDescription7", getPeriodDescription7());
        if (getPeriodDescription7() != null && getPeriodDescription7().length() > 0) {
            write(writer, "LastYearConsumption7", getLastYearConsumption7());
            write(writer, "ThisYearConsumption7", getThisYearConsumption7());
        }
        writeCDataEx(writer, "PeriodDescription8", getPeriodDescription8());
        if (getPeriodDescription8() != null && getPeriodDescription8().length() > 0) {
            write(writer, "LastYearConsumption8", getLastYearConsumption8());
            write(writer, "ThisYearConsumption8", getThisYearConsumption8());
        }
        writeCDataEx(writer, "PeriodDescription9", getPeriodDescription9());
        if (getPeriodDescription9() != null && getPeriodDescription9().length() > 0) {
            write(writer, "LastYearConsumption9", getLastYearConsumption9());
            write(writer, "ThisYearConsumption9", getThisYearConsumption9());
        }
        writeCDataEx(writer, "PeriodDescription10", getPeriodDescription10());
        if (getPeriodDescription10() != null && getPeriodDescription10().length() > 0) {
            write(writer, "LastYearConsumption10", getLastYearConsumption10());
            write(writer, "ThisYearConsumption10", getThisYearConsumption10());
        }
        writeCDataEx(writer, "PeriodDescription11", getPeriodDescription11());
        if (getPeriodDescription11() != null && getPeriodDescription11().length() > 0) {
            write(writer, "LastYearConsumption11", getLastYearConsumption11());
            write(writer, "ThisYearConsumption11", getThisYearConsumption11());
        }
        writeCDataEx(writer, "PeriodDescription12", getPeriodDescription12());
        if (getPeriodDescription12() != null && getPeriodDescription12().length() > 0) {
            write(writer, "LastYearConsumption12", getLastYearConsumption12());
            write(writer, "ThisYearConsumption12", getThisYearConsumption12());
        }
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        writer.writeEndElement();
    }
}