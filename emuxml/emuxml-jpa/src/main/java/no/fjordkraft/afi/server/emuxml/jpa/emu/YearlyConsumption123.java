package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class YearlyConsumption123 extends BaseEmuParser implements EmuParser {

    public static final String YEARLYCONSUMPTION_123 = "YearlyConsumption-123";

    String AnnualConsumption;
    String B_AccountAmount;
    String PriceDate;
    String AnnualCost;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setAnnualConsumption(getField("AnnualConsumption", streamReader, YEARLYCONSUMPTION_123));
        setB_AccountAmount(getField("B-AccountAmount", streamReader, YEARLYCONSUMPTION_123));
        setPriceDate(getField("PriceDate", streamReader, YEARLYCONSUMPTION_123));
        setAnnualCost(getField("AnnualCost", streamReader, YEARLYCONSUMPTION_123));
        checkEnded(YEARLYCONSUMPTION_123, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer)
            throws XMLStreamException {
        writer.writeStartElement(YEARLYCONSUMPTION_123);
        write(writer, "AnnualConsumption", getAnnualConsumption());
        write(writer, "B-AccountAmount", getB_AccountAmount());
        write(writer, "PriceDate", formatDate(getPriceDate()));
        if (getAnnualCost() != null && getAnnualCost().length() > 0) {
            write(writer, "AnnualCost", getAnnualCost());
        }
        writer.writeEndElement();
    }
}
