package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class ActivityOverview147 extends BaseEmuParser implements EmuParser {

    public static final String ACTIVITYOVERVIEW_147 = "ActivityOverview-147";

    String ActivityId;
    String ActivityName;
    String Net;
    String Gross;
    String Vat;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setActivityId(getField("ActivityId", streamReader, ACTIVITYOVERVIEW_147));
        setActivityName(getField("ActivityName", streamReader, ACTIVITYOVERVIEW_147));
        setNet(getField("Net", streamReader, ACTIVITYOVERVIEW_147));
        setGross(getField("Gross", streamReader, ACTIVITYOVERVIEW_147));
        setVat(getField("Vat", streamReader, ACTIVITYOVERVIEW_147));
        checkEnded(ACTIVITYOVERVIEW_147, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer)
            throws XMLStreamException {
        writer.writeStartElement(ACTIVITYOVERVIEW_147);
        write(writer, "ActivityId", getActivityId());
        write(writer, "ActivityName", getActivityName());
        write(writer, "Net", getNet());
        write(writer, "Gross", getGross());
        write(writer, "Vat", getVat());
        writer.writeEndElement();
    }

}