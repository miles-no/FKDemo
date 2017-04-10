package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class InvoiceLine120 extends BaseEmuParser implements EmuParser {

    public static final String INVOICELINE_120 = "InvoiceLine-120";

    String StartDate;
    String EndDate;
    String StartMonth;
    String EndMonth;
    String Net;
    String Gross;
    //        String VAT;
    String StatisticCode;
    String VatRate;
    String SuppCode;
    String Text;
    String PriceCode;
    String PriceRate;
    String Basis;
    String NoOfDays;
    String OldC_Reading;
    String FinalC_Reading;
    String TotalTaxPrice;
    String UnitCode;
    String ProdId;
    String ProdDenomination;
    String InvoiceNo;
    String GrossPrice;
    String TotalGross;
    String PriceDenomination;
    String MiscCode;
    String ArticleNo;
    String B_Account;
    String Type;
    String VAT;
    String DiscPercent;
    String GrossPriceWithoutTax;
    String TotalGrossWithoutTax;
    String TotalTaxPrice_high_precision;
    String GrossPrice_high_precision;
    String GrossPriceWithoutTax_high_precision;
    String ActivityType;
    String ActivitySubType;
    String ServiceAgreeSerialNo;
    String ProductCodePass_ThroughInvoice;
    String PriceDenominationGross;
    String PriceDenominationGrossWithoutTax;
    String PriceDenominationTax;
    String miscSequenceNo;
    String SuppCodeNew;
    String MiscCodeNew;
    String TextNew;
    String DiscAmount;
    String SuppSubscriptionPrice;
    String FireValue;
    String ProductSaleId;
    String Presentation;
    String PriceCodeNew;
    String ExternalAddressId;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setStartDate(getField("StartDate", streamReader, INVOICELINE_120));
        setEndDate(getField("EndDate", streamReader, INVOICELINE_120));
        setStartMonth(getField("StartMonth", streamReader, INVOICELINE_120));
        setEndMonth(getField("EndMonth", streamReader, INVOICELINE_120));
        setNet(getField("Net", streamReader, INVOICELINE_120));
        setGross(getField("Gross", streamReader, INVOICELINE_120));
        setVAT(getField("VAT", streamReader, INVOICELINE_120));
        setStatisticCode(getField("StatisticCode", streamReader, INVOICELINE_120));
        setVatRate(getField("VatRate", streamReader, INVOICELINE_120));
        setSuppCode(getField("SuppCode", streamReader, INVOICELINE_120));
        setText(getField("Text", streamReader, INVOICELINE_120));
        setPriceCode(getField("PriceCode", streamReader, INVOICELINE_120));
        setPriceRate(getField("PriceRate", streamReader, INVOICELINE_120));
        setBasis(getField("Basis", streamReader, INVOICELINE_120));
        setNoOfDays(getField("NoOfDays", streamReader, INVOICELINE_120));
        setOldC_Reading(getField("OldC-Reading", streamReader, INVOICELINE_120));
        setFinalC_Reading(getField("FinalC-Reading", streamReader, INVOICELINE_120));
        setTotalTaxPrice(getField("TotalTaxPrice", streamReader, INVOICELINE_120));
        setUnitCode(getField("UnitCode", streamReader, INVOICELINE_120));
        setProdId(getField("ProdId", streamReader, INVOICELINE_120));
        setProdDenomination(getField("ProdDenomination", streamReader, INVOICELINE_120));
        setInvoiceNo(getField("InvoiceNo", streamReader, INVOICELINE_120));
        setGrossPrice(getField("GrossPrice", streamReader, INVOICELINE_120));
        setTotalGross(getField("TotalGross", streamReader, INVOICELINE_120));
        setPriceDenomination(getField("PriceDenomination", streamReader, INVOICELINE_120));
        setMiscCode(getField("MiscCode", streamReader, INVOICELINE_120));
        setArticleNo(getField("ArticleNo", streamReader, INVOICELINE_120));
        setB_Account(getField("B-Account", streamReader, INVOICELINE_120));
        setType(getField("Type", streamReader, INVOICELINE_120));
        setVAT(getField("VAT", streamReader, INVOICELINE_120));
        setDiscPercent(getField("DiscPercent", streamReader, INVOICELINE_120));
        setGrossPriceWithoutTax(getField("GrossPriceWithoutTax", streamReader, INVOICELINE_120));
        setTotalGrossWithoutTax(getField("TotalGrossWithoutTax", streamReader, INVOICELINE_120));
        setTotalTaxPrice_high_precision(getField("TotalTaxPrice_high_precision", streamReader, INVOICELINE_120));
        setGrossPrice_high_precision(getField("GrossPrice_high_precision", streamReader, INVOICELINE_120));
        setGrossPriceWithoutTax_high_precision(getField("GrossPriceWithoutTax_high_precision", streamReader, INVOICELINE_120));
        setActivityType(getField("ActivityType", streamReader, INVOICELINE_120));
        setActivitySubType(getField("ActivitySubType", streamReader, INVOICELINE_120));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, INVOICELINE_120));
        setProductCodePass_ThroughInvoice(getField("ProductCodePass-ThroughInvoice", streamReader, INVOICELINE_120));
        setPriceDenominationGross(getField("PriceDenominationGross", streamReader, INVOICELINE_120));
        setPriceDenominationGrossWithoutTax(getField("PriceDenominationGrossWithoutTax", streamReader, INVOICELINE_120));
        setPriceDenominationTax(getField("PriceDenominationTax", streamReader, INVOICELINE_120));
        setMiscSequenceNo(getField("miscSequenceNo", streamReader, INVOICELINE_120));
        setSuppCodeNew(getField("SuppCodeNew", streamReader, INVOICELINE_120));
        setMiscCodeNew(getField("MiscCodeNew", streamReader, INVOICELINE_120));
        setTextNew(getField("TextNew", streamReader, INVOICELINE_120));
        setDiscAmount(getField("DiscAmount", streamReader, INVOICELINE_120));
        setSuppSubscriptionPrice(getField("SuppSubscriptionPrice", streamReader, INVOICELINE_120));
        setFireValue(getField("FireValue", streamReader, INVOICELINE_120));
        setProductSaleId(getField("ProductSaleId", streamReader, INVOICELINE_120));
        setPresentation(getField("Presentation", streamReader, INVOICELINE_120));
//        setPriceCodeNew(getFieldNull("PriceCodeNew", streamReader, INVOICELINE_120));
//        setExternalAddressId(getFieldNull("ExternalAddressId", streamReader, INVOICELINE_120));
        checkEnded(INVOICELINE_120, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(INVOICELINE_120);
        if (StringUtils.isNotEmpty(getStartDate())) {
            write(writer, "StartDate", formatDate(getStartDate()));
        }
        if (StringUtils.isNotEmpty(getEndDate())) {
            write(writer, "EndDate", formatDate(getEndDate()));
        }
        writeCDataEx(writer, "StartMonth", getStartMonth());
        writeCDataEx(writer, "EndMonth", getEndMonth());
        write(writer, "Net", getNet());
        write(writer, "Gross", getGross());
        writeCDataEx(writer, "StatisticCode", getStatisticCode());
        write(writer, "VatRate", getVatRate());
        writeCDataEx(writer, "SuppCode", getSuppCode());
        writeCDataEx(writer, "Text", getText());
        writeCDataEx(writer, "PriceCode", getPriceCode());
        write(writer, "PriceRate", getPriceRate());
        write(writer, "Basis", getBasis());
        write(writer, "NoOfDays", getNoOfDays());
        write(writer, "OldC-Reading", getOldC_Reading());
        write(writer, "FinalC-Reading", getFinalC_Reading());
        write(writer, "TotalTaxPrice", getTotalTaxPrice());
        write(writer, "UnitCode", getUnitCode());
        writeCDataEx(writer, "ProdId", getProdId());
        writeCDataEx(writer, "ProdDenomination", getProdDenomination());
        write(writer, "GrossPrice", getGrossPrice());
        write(writer, "TotalGross", getTotalGross());
        writeCDataEx(writer, "PriceDenomination", getPriceDenomination());
        writeCDataEx(writer, "MiscCode", getMiscCode());
//            write(writer, "ArticleNo", getArticleNo());
        writeCDataEx(writer, "B-Account", getB_Account());
        writeCDataEx(writer, "Type", getType());
        write(writer, "VAT", getVAT());
        write(writer, "DiscPercent", getDiscPercent());
        write(writer, "GrossPriceWithoutTax", getGrossPriceWithoutTax());
        write(writer, "TotalGrossWithoutTax", getTotalGrossWithoutTax());
        write(writer, "TotalTaxPrice_high_precision", getTotalTaxPrice_high_precision());
        write(writer, "GrossPrice_high_precision", getGrossPrice_high_precision());
        write(writer, "GrossPriceWithoutTax_high_precision", getGrossPriceWithoutTax_high_precision());
        writeCDataEx(writer, "ActivityType", getActivityType());
        writeCDataEx(writer, "ActivitySubType", getActivitySubType());
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        write(writer, "ProductCodePass-ThroughInvoice", getProductCodePass_ThroughInvoice());
        writeCDataEx(writer, "PriceDenominationGross", getPriceDenominationGross());
        writeCDataEx(writer, "PriceDenominationGrossWithoutTax", getPriceDenominationGrossWithoutTax());
        write(writer, "PriceDenominationTax", getPriceDenominationTax());
        if (StringUtils.isNotEmpty(getMiscSequenceNo())) {
            write(writer, "miscSequenceNo", getMiscSequenceNo());
        }
        if (getSuppCodeNew() != null && getSuppCodeNew().length() > 0) {
            write(writer, "SuppCodeNew", getSuppCodeNew());
        }
        write(writer, "MiscCodeNew", getMiscCodeNew());
        writeCDataEx(writer, "TextNew", getTextNew());
        write(writer, "DiscAmount", getDiscAmount());
        write(writer, "SuppSubscriptionPrice", getSuppSubscriptionPrice());
        write(writer, "FireValue", getFireValue());
        write(writer, "ProductSaleId", getProductSaleId());
//            write(writer, "Presentation", getPresentation());
        if (StringUtils.isNotEmpty(getPriceCodeNew())) {
            writeCDataEx(writer, "PriceCode", getPriceCodeNew());
        }
        if (StringUtils.isNotEmpty(getExternalAddressId())) {
            writeCDataEx(writer, "ExternalAddressId", getExternalAddressId());
        }
        writer.writeEndElement();
    }
}