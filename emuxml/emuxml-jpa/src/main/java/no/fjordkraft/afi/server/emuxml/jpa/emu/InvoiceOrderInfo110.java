package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class InvoiceOrderInfo110 extends BaseEmuParser implements EmuParser {

    public static final String INVOICEORDERINFO_110 = "InvoiceOrderInfo-110";

    String StartDate;
    String BillingGroup;
    String ServiceAgreeSerialNo;
    String EndDate;
    String CloseCode;
    String TotalPrinted;
    String ShortTimeSA;
    String IndustryCode;
    String B_AccountCode;
    String InvoiceType;
    String ConsumptionCode;
    String AreaId;
    String LDC1;
    String LDC2;
    String Supplier1;
    String Supplier2;
    String B_AccountText;
    String Amount;
    String Instlmnt;
    String ActivityId;
    String ActivityType;
    String ActivitySubType;
    String ActivityName;
    String CompanyRegNo;
    String UtilityAccountNo;
    String ActivitySubTypeName;
    String YearEffect;
    String ActivityCloseable;
    String IndustryCodeNew;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setStartDate(getField("StartDate", streamReader, INVOICEORDERINFO_110));
        setBillingGroup(getField("BillingGroup", streamReader, INVOICEORDERINFO_110));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, INVOICEORDERINFO_110));
        setEndDate(getField("EndDate", streamReader, INVOICEORDERINFO_110));
        setCloseCode(getField("CloseCode", streamReader, INVOICEORDERINFO_110));
        setTotalPrinted(getField("TotalPrinted", streamReader, INVOICEORDERINFO_110));
        setShortTimeSA(getField("ShortTimeSA", streamReader, INVOICEORDERINFO_110));
        setIndustryCode(getField("IndustryCode", streamReader, INVOICEORDERINFO_110));
        setB_AccountCode(getField("B-AccountCode", streamReader, INVOICEORDERINFO_110));
        setInvoiceType(getField("InvoiceType", streamReader, INVOICEORDERINFO_110));
        setConsumptionCode(getField("ConsumptionCode", streamReader, INVOICEORDERINFO_110));
        setAreaId(getField("AreaId", streamReader, INVOICEORDERINFO_110));
        setLDC1(getField("LDC1", streamReader, INVOICEORDERINFO_110));
        setLDC2(getField("LDC2", streamReader, INVOICEORDERINFO_110));
        setSupplier1(getField("Supplier1", streamReader, INVOICEORDERINFO_110));
        setSupplier2(getField("Supplier2", streamReader, INVOICEORDERINFO_110));
        setB_AccountText(getField("B-AccountText", streamReader, INVOICEORDERINFO_110));
        setAmount(getField("Amount", streamReader, INVOICEORDERINFO_110));
        setInstlmnt(getField("Instlmnt", streamReader, INVOICEORDERINFO_110));
        setActivityId(getField("ActivityId", streamReader, INVOICEORDERINFO_110));
        setActivityType(getField("ActivityType", streamReader, INVOICEORDERINFO_110));
        setActivitySubType(getField("ActivitySubType", streamReader, INVOICEORDERINFO_110));
        setActivityName(getField("ActivityName", streamReader, INVOICEORDERINFO_110));
        setCompanyRegNo(getField("CompanyRegNo", streamReader, INVOICEORDERINFO_110));
        setUtilityAccountNo(getField("UtilityAccountNo", streamReader, INVOICEORDERINFO_110));
        setActivitySubTypeName(getField("ActivitySubTypeName", streamReader, INVOICEORDERINFO_110));
        setYearEffect(getField("YearEffect", streamReader, INVOICEORDERINFO_110));
        setActivityCloseable(getField("ActivityCloseable", streamReader, INVOICEORDERINFO_110));
//        setIndustryCodeNew(getFieldNull("IndustryCodeNew", streamReader, INVOICEORDERINFO_110));
        checkEnded(INVOICEORDERINFO_110, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(INVOICEORDERINFO_110);
        if (StringUtils.isNotEmpty(getStartDate())) {
            write(writer, "StartDate", formatDate(getStartDate()));
        }
        writeCDataEx(writer, "BillingGroup", getBillingGroup());
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        if (StringUtils.isNotEmpty(getEndDate())) {
            write(writer, "EndDate", formatDate(getEndDate()));
        }
        writeCDataEx(writer, "CloseCode", getCloseCode());
        write(writer, "TotalPrinted", getTotalPrinted());
        writeCDataEx(writer, "ShortTimeSA", getShortTimeSA());
        writeCDataEx(writer, "IndustryCode", getIndustryCode());
        writeCDataEx(writer, "B-AccountCode", getB_AccountCode());
        writeCDataEx(writer, "InvoiceType", getInvoiceType());
        writeCDataEx(writer, "ConsumptionCode", getConsumptionCode());
        writeCDataEx(writer, "AreaId", getAreaId());
        writeCDataEx(writer, "LDC1", getLDC1());
        writeCDataEx(writer, "LDC2", getLDC2());
        writeCDataEx(writer, "Supplier1", getSupplier1());
        writeCDataEx(writer, "Supplier2", getSupplier2());
        writeCDataEx(writer, "B-AccountText", getB_AccountText());
        write(writer, "Amount", getAmount());
        write(writer, "Instlmnt", getInstlmnt());
        writeCDataEx(writer, "ActivityId", getActivityId());
        writeCDataEx(writer, "ActivityType", getActivityType());
        writeCDataEx(writer, "ActivitySubType", getActivitySubType());
        writeCDataEx(writer, "ActivityName", getActivityName());
        writeCDataEx(writer, "CompanyRegNo", getCompanyRegNo());
        writeCDataEx(writer, "UtilityAccountNo", getUtilityAccountNo());
        writeCDataEx(writer, "ActivitySubTypeName", getActivitySubTypeName());
        write(writer, "YearEffect", getYearEffect());
        writeCDataEx(writer, "ActivityCloseable", getActivityCloseable());
        if (StringUtils.isNotEmpty(getIndustryCodeNew())) {
            writeCDataEx(writer, "IndustryCodeNew", getIndustryCodeNew());
        }
        writer.writeEndElement();
    }
}