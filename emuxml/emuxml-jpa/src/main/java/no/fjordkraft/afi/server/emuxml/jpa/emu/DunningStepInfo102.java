package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class DunningStepInfo102 extends BaseEmuParser implements EmuParser {

    public static final String DUNNINGSTEPINFO_102 = "DunningStepInfo-102";

    String DunningStep;
    String DunningCharge;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setDunningStep(getField("DunningStep", streamReader, DUNNINGSTEPINFO_102));
        setDunningCharge(getField("DunningCharge", streamReader, DUNNINGSTEPINFO_102));
        checkEnded(DUNNINGSTEPINFO_102, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(DUNNINGSTEPINFO_102);
        write(writer, "DunningStep", getDunningStep());
        write(writer, "DunningCharge", getDunningCharge());
        writer.writeEndElement();
    }

}