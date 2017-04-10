package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class InvoiceAgreementInfo105 extends BaseEmuParser implements EmuParser {

    public static final String INVOICEAGREEMENTINFO_105 = "InvoiceAgreementInfo-105";

    String InvAgreementId;
    String AgreementDate;
    String IndustryPrivate;
    String InvoiceLabel;
    String InterestId;
    String InterestRate;
    String PayerStatus;
    String AccountNo;
    String TotalPrinted;
    String TotalPrintedOtherInvoices;
    String BIC_Customer;
    String IBAN_Customer;
    String InvoiceLabelNew;
    String OrderReference;
    String AccountingCost;


    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setInvAgreementId(getField("InvAgreementId", streamReader, INVOICEAGREEMENTINFO_105));
        setAgreementDate(getField("AgreementDate", streamReader, INVOICEAGREEMENTINFO_105));
        setIndustryPrivate(getField("IndustryPrivate", streamReader, INVOICEAGREEMENTINFO_105));
        setInvoiceLabel(getField("InvoiceLabel", streamReader, INVOICEAGREEMENTINFO_105));
        setInterestId(getField("InterestId", streamReader, INVOICEAGREEMENTINFO_105));
        setInterestRate(getField("InterestRate", streamReader, INVOICEAGREEMENTINFO_105));
        setPayerStatus(getField("PayerStatus", streamReader, INVOICEAGREEMENTINFO_105));
        setAccountNo(getField("AccountNo", streamReader, INVOICEAGREEMENTINFO_105));
        setTotalPrinted(getField("TotalPrinted", streamReader, INVOICEAGREEMENTINFO_105));
        setTotalPrintedOtherInvoices(getField("TotalPrintedOtherInvoices", streamReader, INVOICEAGREEMENTINFO_105));
        setBIC_Customer(getField("BIC-Customer", streamReader, INVOICEAGREEMENTINFO_105));
        setIBAN_Customer(getField("IBAN-Customer", streamReader, INVOICEAGREEMENTINFO_105));
//        setInvoiceLabelNew(getFieldNull("InvoiceLabelNew", streamReader, INVOICEAGREEMENTINFO_105));
//        setOrderReference(getFieldNull("OrderReference", streamReader, INVOICEAGREEMENTINFO_105));
//        setAccountingCost(getFieldNull("AccountingCost", streamReader, INVOICEAGREEMENTINFO_105));
        checkEnded(INVOICEAGREEMENTINFO_105, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(INVOICEAGREEMENTINFO_105);
        write(writer, "InvAgreementId", getInvAgreementId());
        write(writer, "AgreementDate", formatDate(getAgreementDate()));
        writeCDataEx(writer, "IndustryPrivate", getIndustryPrivate());
        writeCDataEx(writer, "InvoiceLabel", getInvoiceLabel());
        writeCDataEx(writer, "InterestId", getInterestId());
        write(writer, "InterestRate", getInterestRate());
        writeCDataEx(writer, "PayerStatus", getPayerStatus());
        writeCDataEx(writer, "AccountNo", getAccountNo());
        write(writer, "TotalPrinted", getTotalPrinted());
        write(writer, "TotalPrintedOtherInvoices", getTotalPrintedOtherInvoices());
        write(writer, "BIC-Customer", getBIC_Customer());
        write(writer, "IBAN-Customer", getIBAN_Customer());
//        write(writer, "InvoiceLabelNew", getInvoiceLabelNew());
//        write(writer, "OrderReference", getOrderReference());
//        write(writer, "AccountingCost", getAccountingCost());
        writer.writeEndElement();
    }
}
