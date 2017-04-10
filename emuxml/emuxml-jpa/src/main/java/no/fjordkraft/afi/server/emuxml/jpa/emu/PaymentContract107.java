package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class PaymentContract107 extends BaseEmuParser implements EmuParser {

    public static final String PAYMENTCONTRACT_107 = "PaymentContract-107";

    String Id;
    String CalculateInterest;
    String CreatedDate;
    String Status;
    String ClosedDate;
    String PrintDate;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setId(getField("Id", streamReader, PAYMENTCONTRACT_107));
        setCalculateInterest(getField("CalculateInterest", streamReader, PAYMENTCONTRACT_107));
        setCreatedDate(getField("CreatedDate", streamReader, PAYMENTCONTRACT_107));
        setStatus(getField("Status", streamReader, PAYMENTCONTRACT_107));
        setClosedDate(getField("ClosedDate", streamReader, PAYMENTCONTRACT_107));
        setPrintDate(getField("PrintDate", streamReader, PAYMENTCONTRACT_107));
        checkEnded(PAYMENTCONTRACT_107, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(PAYMENTCONTRACT_107);
        write(writer, "Id", getId());
        write(writer, "CalculateInterest", getCalculateInterest());
        write(writer, "CreatedDate", formatDate(getCreatedDate()));
        write(writer, "Status", getStatus());
        write(writer, "ClosedDate", formatDate(getClosedDate()));
        write(writer, "PrintDate", formatDate(getPrintDate()));
        writer.writeEndElement();
    }

}