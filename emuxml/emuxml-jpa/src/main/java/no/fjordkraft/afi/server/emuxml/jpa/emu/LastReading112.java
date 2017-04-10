package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class LastReading112 extends BaseEmuParser implements EmuParser {

    public static final String LASTREADING_112 = "LastReading-112";

    String DateRead;
    String ReadingMethod;
    String MeterCounter1;
    String C_Reading1;
    String MeterCounter2;
    String C_Reading2;
    String MeterCounter3;
    String C_Reading3;
    String MethodText;
    String ServiceAgreeSerialNo;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setDateRead(getField("DateRead", streamReader, LASTREADING_112));
        setReadingMethod(getField("ReadingMethod", streamReader, LASTREADING_112));
        setMeterCounter1(getField("MeterCounter1", streamReader, LASTREADING_112));
        setC_Reading1(getField("C-Reading1", streamReader, LASTREADING_112));
        setMeterCounter2(getField("MeterCounter2", streamReader, LASTREADING_112));
        setC_Reading2(getField("C-Reading2", streamReader, LASTREADING_112));
        setMeterCounter3(getField("MeterCounter3", streamReader, LASTREADING_112));
        setC_Reading3(getField("C-Reading3", streamReader, LASTREADING_112));
        setMethodText(getField("MethodText", streamReader, LASTREADING_112));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, LASTREADING_112));
        checkEnded(LASTREADING_112, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(LASTREADING_112);
        write(writer, "DateRead", formatDate(getDateRead()));
        writeCDataEx(writer, "ReadingMethod", getReadingMethod());
        write(writer, "MeterCounter1", getMeterCounter1());
        write(writer, "C-Reading1", getC_Reading1());
        if (StringUtils.isNotEmpty(getMeterCounter2())) {
            write(writer, "MeterCounter2", getMeterCounter2());
        }
        if (StringUtils.isNotEmpty(getC_Reading2())) {
            write(writer, "C-Reading2", getC_Reading2());
        }
        if (StringUtils.isNotEmpty(getMeterCounter3())) {
            write(writer, "MeterCounter3", getMeterCounter3());
        }
        if (StringUtils.isNotEmpty(getC_Reading3())) {
            write(writer, "C-Reading3", getC_Reading3());
        }
        writeCDataEx(writer, "MethodText", getMethodText());
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        writer.writeEndElement();
    }
}