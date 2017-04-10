package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.text.DecimalFormat;

@Slf4j
public abstract class BaseEmuParser {

    protected String getField(final String field, XMLStreamReader streamReader, String endField)
            throws XMLStreamException, RuntimeException {
        return getField(field, streamReader, endField, false);
    }

    protected String getFieldNull(final String field, XMLStreamReader streamReader, String endField)
            throws XMLStreamException, RuntimeException {
        return getField(field, streamReader, endField, true);
    }

    private String getField(final String field, XMLStreamReader streamReader, String endField, boolean canBeNull)
            throws XMLStreamException, RuntimeException {
        while (streamReader.hasNext()) {
            streamReader.next();
            if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT && field.equals(streamReader.getLocalName())) {
                return streamReader.getElementText();
            } else if (streamReader.getEventType() == XMLStreamReader.END_ELEMENT && endField.equals(streamReader.getLocalName())) {
                break;
            }
        }
        if (!canBeNull) {
            throw new RuntimeException(String.format("Missing field: %s", field));
        }
        return null;
    }

    protected void checkEnded(final String endField, XMLStreamReader streamReader)
            throws XMLStreamException {
        while (streamReader.hasNext()) {
            streamReader.next();
            if (streamReader.getEventType() == XMLStreamReader.END_ELEMENT) {
                if (endField.equals(streamReader.getLocalName())) {
                    return;
                }
            }
        }
        throw new RuntimeException(String.format("Missing end field: %s", endField));
    }

    protected void write(XMLStreamWriter writer, String field, String value)
            throws XMLStreamException {
        writer.writeStartElement(field);
        if (value != null) {
            writer.writeCharacters(value);
        }
        writer.writeEndElement();
    }

    protected void writeCData(XMLStreamWriter writer, String field, String value)
            throws XMLStreamException {
        writer.writeStartElement(field);
        if (value != null) {
            writer.writeCData(value);
        }
        writer.writeEndElement();
    }

    protected void writeCDataEx(XMLStreamWriter writer, String field, String value)
            throws XMLStreamException {
        writer.writeStartElement(field);
        writer.writeCharacters(value != null && value.length() > 0 ?
                "<![CDATA[" + value + "]]>" : "");
        writer.writeEndElement();
    }

    static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd.MM.YYYY");
    static DateTimeFormatter dateISO8601Formatter = ISODateTimeFormat.date();

    protected String formatDate(String date) {
        String formatted = date;
        if (date != null && date.length() > 0) {
            try {
                formatted = dateISO8601Formatter.print(dateFormatter.parseDateTime(date));
            } catch (Throwable t) {
                throw new RuntimeException("invalid date format: " + date);
            }
        }
        return formatted;
    }

    protected String fixAmount(String amount) {
        try {
            return new DecimalFormat("###.##").format(new Double(amount)).replace(",", ".");
        } catch (NumberFormatException e) {
            log.error(String.format("Parse amount exception: %s %s", amount, e.getMessage()));
        }
        return amount;
    }
}
