package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class InvoiceDunningInfo106 extends BaseEmuParser implements EmuParser {

    public static final String INVOICEDUNNINGINFO_106 = "InvoiceDunningInfo-106";

    String FormTmplCode;
    String Text;
    String Amount;
    String DueDate;
    String Closure;
    String DunningCount;
    String PrintDate;
    String DunningActionId;
    String AmountWithoutDec;
    String Decimals;
    String Mod10;
    String TotalPrintedInvoice;
    String NoOfPrevClosures;
    String NoOfPrevDunnings;
    String ProductDescription;
    String TextNew;
    String AmountBeforeDunning;
    String AlreadyPaid;
    String ProdDate;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setFormTmplCode(getField("FormTmplCode", streamReader, INVOICEDUNNINGINFO_106));
        setText(getField("Text", streamReader, INVOICEDUNNINGINFO_106));
        setAmount(getField("Amount", streamReader, INVOICEDUNNINGINFO_106));
        setDueDate(getField("DueDate", streamReader, INVOICEDUNNINGINFO_106));
        setClosure(getField("Closure", streamReader, INVOICEDUNNINGINFO_106));
        setDunningCount(getField("DunningCount", streamReader, INVOICEDUNNINGINFO_106));
        setPrintDate(getField("PrintDate", streamReader, INVOICEDUNNINGINFO_106));
        setDunningActionId(getField("DunningActionId", streamReader, INVOICEDUNNINGINFO_106));
        setAmountWithoutDec(getField("AmountWithoutDec", streamReader, INVOICEDUNNINGINFO_106));
        setDecimals(getField("Decimals", streamReader, INVOICEDUNNINGINFO_106));
        setMod10(getField("Mod10", streamReader, INVOICEDUNNINGINFO_106));
        setTotalPrintedInvoice(getField("TotalPrintedInvoice", streamReader, INVOICEDUNNINGINFO_106));
        setNoOfPrevClosures(getField("NoOfPrevClosures", streamReader, INVOICEDUNNINGINFO_106));
        setNoOfPrevDunnings(getField("NoOfPrevDunnings", streamReader, INVOICEDUNNINGINFO_106));
        setProductDescription(getField("ProductDescription", streamReader, INVOICEDUNNINGINFO_106));
        setTextNew(getField("TextNew", streamReader, INVOICEDUNNINGINFO_106));
        setAmountBeforeDunning(getField("AmountBeforeDunning", streamReader, INVOICEDUNNINGINFO_106));
        setAlreadyPaid(getField("AlreadyPaid", streamReader, INVOICEDUNNINGINFO_106));
        setProdDate(getField("ProdDate", streamReader, INVOICEDUNNINGINFO_106));
        checkEnded(INVOICEDUNNINGINFO_106, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(INVOICEDUNNINGINFO_106);
        write(writer, "FormTmplCode", getFormTmplCode());
        write(writer, "Text", getText());
        write(writer, "Amount", getAmount());
        write(writer, "DueDate", formatDate(getDueDate()));
        write(writer, "Closure", getClosure());
        write(writer, "DunningCount", getDunningCount());
        write(writer, "PrintDate", formatDate(getPrintDate()));
        write(writer, "DunningActionId", getDunningActionId());
        write(writer, "AmountWithoutDec", getAmountWithoutDec());
        write(writer, "Decimals", getDecimals());
        write(writer, "Mod10", getMod10());
        write(writer, "TotalPrintedInvoice", getTotalPrintedInvoice());
        write(writer, "NoOfPrevClosures", getNoOfPrevClosures());
        write(writer, "NoOfPrevDunnings", getNoOfPrevDunnings());
        write(writer, "ProductDescription", getProductDescription());
        write(writer, "TextNew", getTextNew());
        write(writer, "AmountBeforeDunning", getAmountBeforeDunning());
        write(writer, "AlreadyPaid", getAlreadyPaid());
        write(writer, "ProdDate", formatDate(getProdDate()));
        writer.writeEndElement();
    }

}