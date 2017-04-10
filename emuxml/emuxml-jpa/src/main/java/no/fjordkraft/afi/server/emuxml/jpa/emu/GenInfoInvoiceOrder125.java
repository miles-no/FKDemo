package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class GenInfoInvoiceOrder125 extends BaseEmuParser implements EmuParser {

    public static final String GENINFOINVOICEORDER_125 = "GenInfoInvoiceOrder-125";

    String TextCode;
    String PageIndicator;
    String Text;
    String ActivityType;
    String ActivitySubType;
    String ServiceAgreeSerialNo;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setTextCode(getField("TextCode", streamReader, GENINFOINVOICEORDER_125));
        setPageIndicator(getField("PageIndicator", streamReader, GENINFOINVOICEORDER_125));
        setText(getField("Text", streamReader, GENINFOINVOICEORDER_125));
        setActivityType(getField("ActivityType", streamReader, GENINFOINVOICEORDER_125));
        setActivitySubType(getField("ActivitySubType", streamReader, GENINFOINVOICEORDER_125));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, GENINFOINVOICEORDER_125));
        checkEnded(GENINFOINVOICEORDER_125, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(GENINFOINVOICEORDER_125);
        writeCDataEx(writer, "TextCode", getTextCode());
        writeCDataEx(writer, "PageIndicator", getPageIndicator());
        writeCDataEx(writer, "Text", getText());
        writeCDataEx(writer, "ActivityType", getActivityType());
        writeCDataEx(writer, "ActivitySubType", getActivitySubType());
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        writer.writeEndElement();
    }
}