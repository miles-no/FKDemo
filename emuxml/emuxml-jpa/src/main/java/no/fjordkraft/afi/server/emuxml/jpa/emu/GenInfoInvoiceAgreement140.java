package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class GenInfoInvoiceAgreement140 extends BaseEmuParser implements EmuParser {

    public static final String GENINFOINVOICEAGREEMENT_140 = "GenInfoInvoiceAgreement-140";

    String TextCode;
    String PageIndicator;
    String Text;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setTextCode(getField("TextCode", streamReader, GENINFOINVOICEAGREEMENT_140));
        setPageIndicator(getField("PageIndicator", streamReader, GENINFOINVOICEAGREEMENT_140));
        setText(getField("Text", streamReader, GENINFOINVOICEAGREEMENT_140));
        checkEnded(GENINFOINVOICEAGREEMENT_140, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer)
            throws XMLStreamException {
        writer.writeStartElement(GENINFOINVOICEAGREEMENT_140);
        write(writer, "TextCode", getTextCode());
        write(writer, "PageIndicator", getPageIndicator());
        write(writer, "Text", getText());
        writer.writeEndElement();
    }

}