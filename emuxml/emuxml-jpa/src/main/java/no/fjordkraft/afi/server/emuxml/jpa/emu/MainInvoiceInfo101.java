package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class MainInvoiceInfo101 extends BaseEmuParser implements EmuParser {

    public static final String MAIN_INVOICE_INFO_101 = "MainInvoiceInfo-101";

    String InvoiceNo;
    String CustomerId;
    String CID;
    String PrintDate;
    String DueDate;
    String PayInst2Total;
    String PaymentDocNo;
    String GrossPresentation;
    String E_InvoiceReference;
    String E_InvoiceTmpl;
    String StandingOrder;
    String ProcessCode;
    //        String Utility;
    String FormTmplCode;
    String FormType;
    String DateCredited;
    String DueDateDescription;
    String MoraInterest;
    String PrintedAmount;
    String PreviouslyPaid;
    String ToBePaid;
    String VAT;
    String TotPrintWithoutDec;
    String Decimals;
    String Mod10;
    String TotalOutstanding;
    String EInvoiceUrl;
    String Utility;
    String ReminderFee;
    String ToBePaidNet;
    String InvoiceDate;
    String CIDLong;
    String IsTestInvoice;
    String CreditedInvoiceNo;
    String E_InvoiceReferenceNew;
    String DueDateCreditedInvoice;
    String netPrintet;
    String RegulationRecords;
    String TemplateType;
    String Transactions;
    String Balance;
    String EInvoiceReferenceProposal;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setInvoiceNo(getField("InvoiceNo", streamReader, MAIN_INVOICE_INFO_101));
        setCustomerId(getField("CustomerId", streamReader, MAIN_INVOICE_INFO_101));
        setCID(getField("CID", streamReader, MAIN_INVOICE_INFO_101));
        setPrintDate(getField("PrintDate", streamReader, MAIN_INVOICE_INFO_101));
        setDueDate(getField("DueDate", streamReader, MAIN_INVOICE_INFO_101));
        setPayInst2Total(getField("PayInst2Total", streamReader, MAIN_INVOICE_INFO_101));
        setPaymentDocNo(getField("PaymentDocNo", streamReader, MAIN_INVOICE_INFO_101));
        setGrossPresentation(getField("GrossPresentation", streamReader, MAIN_INVOICE_INFO_101));
        setE_InvoiceReference(getField("E-InvoiceReference", streamReader, MAIN_INVOICE_INFO_101));
        setE_InvoiceTmpl(getField("E-InvoiceTmpl", streamReader, MAIN_INVOICE_INFO_101));
        setStandingOrder(getField("StandingOrder", streamReader, MAIN_INVOICE_INFO_101));
        setProcessCode(getField("ProcessCode", streamReader, MAIN_INVOICE_INFO_101));
//        setUtility;(getField("Utility;", emuReader, MAIN_INVOICE_INFO_101));
        setFormTmplCode(getField("FormTmplCode", streamReader, MAIN_INVOICE_INFO_101));
        setFormType(getField("FormType", streamReader, MAIN_INVOICE_INFO_101));
        setDateCredited(getField("DateCredited", streamReader, MAIN_INVOICE_INFO_101));
        setDueDateDescription(getField("DueDateDescription", streamReader, MAIN_INVOICE_INFO_101));
        setMoraInterest(getField("MoraInterest", streamReader, MAIN_INVOICE_INFO_101));
        setPrintedAmount(getField("PrintedAmount", streamReader, MAIN_INVOICE_INFO_101));
        setPreviouslyPaid(getField("PreviouslyPaid", streamReader, MAIN_INVOICE_INFO_101));
        setToBePaid(getField("ToBePaid", streamReader, MAIN_INVOICE_INFO_101));
        setVAT(getField("VAT", streamReader, MAIN_INVOICE_INFO_101));
        setTotPrintWithoutDec(getField("TotPrintWithoutDec", streamReader, MAIN_INVOICE_INFO_101));
        setDecimals(getField("Decimals", streamReader, MAIN_INVOICE_INFO_101));
        setMod10(getField("Mod10", streamReader, MAIN_INVOICE_INFO_101));
        setTotalOutstanding(getField("TotalOutstanding", streamReader, MAIN_INVOICE_INFO_101));
        setEInvoiceUrl(getField("EInvoiceUrl", streamReader, MAIN_INVOICE_INFO_101));
        setUtility(getField("Utility", streamReader, MAIN_INVOICE_INFO_101));
        setReminderFee(getField("ReminderFee", streamReader, MAIN_INVOICE_INFO_101));
        setToBePaidNet(getField("ToBePaidNet", streamReader, MAIN_INVOICE_INFO_101));
        setInvoiceDate(getField("InvoiceDate", streamReader, MAIN_INVOICE_INFO_101));
        setCIDLong(getField("CIDLong", streamReader, MAIN_INVOICE_INFO_101));
        setIsTestInvoice(getField("IsTestInvoice", streamReader, MAIN_INVOICE_INFO_101));
        setCreditedInvoiceNo(getField("CreditedInvoiceNo", streamReader, MAIN_INVOICE_INFO_101));
        setE_InvoiceReferenceNew(getField("E-InvoiceReferenceNew", streamReader, MAIN_INVOICE_INFO_101));
        setDueDateCreditedInvoice(getField("DueDateCreditedInvoice", streamReader, MAIN_INVOICE_INFO_101));
        setNetPrintet(getField("netPrintet", streamReader, MAIN_INVOICE_INFO_101));
        setRegulationRecords(getField("RegulationRecords", streamReader, MAIN_INVOICE_INFO_101));
        setTemplateType(getField("TemplateType", streamReader, MAIN_INVOICE_INFO_101));
        setTransactions(getField("Transactions", streamReader, MAIN_INVOICE_INFO_101));
        setBalance(getField("Balance", streamReader, MAIN_INVOICE_INFO_101));
        setEInvoiceReferenceProposal(getField("EInvoiceReferenceProposal", streamReader, MAIN_INVOICE_INFO_101));
        checkEnded(MAIN_INVOICE_INFO_101, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(MAIN_INVOICE_INFO_101);
        write(writer, "InvoiceNo", getInvoiceNo());
        write(writer, "CustomerId", getCustomerId());
        write(writer, "CID", getCID());
        write(writer, "PrintDate", formatDate(getPrintDate()));
        write(writer, "DueDate", formatDate(getDueDate()));
        write(writer, "PayInst2Total", getPayInst2Total());
        write(writer, "PaymentDocNo", getPaymentDocNo());
        writeCDataEx(writer, "GrossPresentation", getGrossPresentation());
        write(writer, "E-InvoiceTmpl", getE_InvoiceTmpl());
        writeCDataEx(writer, "StandingOrder", getStandingOrder());
        writeCDataEx(writer, "ProcessCode", getProcessCode());
        writeCDataEx(writer, "FormTmplCode", getFormTmplCode());
        writeCDataEx(writer, "FormType", getFormType());
        writeCDataEx(writer, "DueDateDescription", getDueDateDescription());
        write(writer, "MoraInterest", getMoraInterest());
        write(writer, "PrintedAmount", getPrintedAmount());
        write(writer, "PreviouslyPaid", getPreviouslyPaid());
        write(writer, "ToBePaid", getToBePaid());
        write(writer, "VAT", getVAT());
        write(writer, "TotPrintWithoutDec", getTotPrintWithoutDec());
        write(writer, "Decimals", getDecimals());
        write(writer, "Mod10", getMod10());
        write(writer, "TotalOutstanding", getTotalOutstanding());
        write(writer, "EInvoiceUrl", getEInvoiceUrl());
        writeCDataEx(writer, "Utility", getUtility());
        write(writer, "ReminderFee", getReminderFee());
        write(writer, "ToBePaidNet", getToBePaidNet());
        write(writer, "InvoiceDate", formatDate(getInvoiceDate()));
        write(writer, "CIDLong", getCIDLong());
        writeCDataEx(writer, "IsTestInvoice", getIsTestInvoice());
        write(writer, "CreditedInvoiceNo", getCreditedInvoiceNo());
        write(writer, "E-InvoiceReferenceNew", getE_InvoiceReferenceNew());
        write(writer, "netPrintet", getNetPrintet());
        write(writer, "RegulationRecords", getRegulationRecords());
        writeCDataEx(writer, "TemplateType", getTemplateType());
        write(writer, "Transactions", getTransactions());
        write(writer, "Balance", getBalance());
        write(writer, "EInvoiceReferenceProposal", getEInvoiceReferenceProposal());
        writer.writeEndElement();
    }

}