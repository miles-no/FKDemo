package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class InvoiceOrderAmounts113 extends BaseEmuParser implements EmuParser {

    public static final String INVOICEORDERAMOUNTS_113 = "InvoiceOrderAmounts-113";

    String GrossSettled;
    String NetSettled;
    String VatSettled;
    String GrossB_Account;
    String NetB_Account;
    String VatB_Account;
    String BAccountTermNo;
    String BAccountTermStartDate;
    String BAccountTermEndDate;
    String GrossWriteBacks;
    String NetWriteBacks;
    String VatWriteBacks;
    String GrossMisc;
    String NetMisc;
    String VatMisc;
    String GrossItem;
    String NetItem;
    String VatItem;
    String VatTotal;
    String GrossTax;
    String NetTotal;
    String GrossTotal;
    String GrossSettledAndWriteBacks;
    String NetSettledAndWriteBacks;
    String VatSettledAndWriteBacks;
    String ServiceAgreeSerialNo;
    String NetWithTax;
    String GrossWithTax;
    String NetNoTax;
    String OutstandingDebt;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setGrossSettled(getField("GrossSettled", streamReader, INVOICEORDERAMOUNTS_113));
        setNetSettled(getField("NetSettled", streamReader, INVOICEORDERAMOUNTS_113));
        setVatSettled(getField("VatSettled", streamReader, INVOICEORDERAMOUNTS_113));
        setGrossB_Account(getField("GrossB-Account", streamReader, INVOICEORDERAMOUNTS_113));
        setNetB_Account(getField("NetB-Account", streamReader, INVOICEORDERAMOUNTS_113));
        setVatB_Account(getField("VatB-Account", streamReader, INVOICEORDERAMOUNTS_113));
        setBAccountTermNo(getField("BAccountTermNo", streamReader, INVOICEORDERAMOUNTS_113));
        setBAccountTermStartDate(getField("BAccountTermStartDate", streamReader, INVOICEORDERAMOUNTS_113));
        setBAccountTermEndDate(getField("BAccountTermEndDate", streamReader, INVOICEORDERAMOUNTS_113));
        setGrossWriteBacks(getField("GrossWriteBacks", streamReader, INVOICEORDERAMOUNTS_113));
        setNetWriteBacks(getField("NetWriteBacks", streamReader, INVOICEORDERAMOUNTS_113));
        setVatWriteBacks(getField("VatWriteBacks", streamReader, INVOICEORDERAMOUNTS_113));
        setGrossMisc(getField("GrossMisc", streamReader, INVOICEORDERAMOUNTS_113));
        setNetMisc(getField("NetMisc", streamReader, INVOICEORDERAMOUNTS_113));
        setVatMisc(getField("VatMisc", streamReader, INVOICEORDERAMOUNTS_113));
        setGrossItem(getField("GrossItem", streamReader, INVOICEORDERAMOUNTS_113));
        setNetItem(getField("NetItem", streamReader, INVOICEORDERAMOUNTS_113));
        setVatItem(getField("VatItem", streamReader, INVOICEORDERAMOUNTS_113));
        setVatTotal(getField("VatTotal", streamReader, INVOICEORDERAMOUNTS_113));
        setGrossTax(getField("GrossTax", streamReader, INVOICEORDERAMOUNTS_113));
        setNetTotal(getField("NetTotal", streamReader, INVOICEORDERAMOUNTS_113));
        setGrossTotal(getField("GrossTotal", streamReader, INVOICEORDERAMOUNTS_113));
        setGrossSettledAndWriteBacks(getField("GrossSettledAndWriteBacks", streamReader, INVOICEORDERAMOUNTS_113));
        setNetSettledAndWriteBacks(getField("NetSettledAndWriteBacks", streamReader, INVOICEORDERAMOUNTS_113));
        setVatSettledAndWriteBacks(getField("VatSettledAndWriteBacks", streamReader, INVOICEORDERAMOUNTS_113));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, INVOICEORDERAMOUNTS_113));
        setNetWithTax(getField("NetWithTax", streamReader, INVOICEORDERAMOUNTS_113));
        setGrossWithTax(getField("GrossWithTax", streamReader, INVOICEORDERAMOUNTS_113));
        setNetNoTax(getField("NetNoTax", streamReader, INVOICEORDERAMOUNTS_113));
//        setOutstandingDebt(getFieldNull("OutstandingDebt", streamReader, INVOICEORDERAMOUNTS_113));
        checkEnded(INVOICEORDERAMOUNTS_113, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(INVOICEORDERAMOUNTS_113);
        write(writer, "GrossSettled", getGrossSettled());
        write(writer, "NetSettled", getNetSettled());
        write(writer, "VatSettled", getVatSettled());
        write(writer, "GrossB-Account", getGrossB_Account());
        write(writer, "NetB-Account", getNetB_Account());
        write(writer, "VatB-Account", getVatB_Account());
        write(writer, "BAccountTermNo", getBAccountTermNo());
        if (StringUtils.isNotEmpty(getBAccountTermStartDate())) {
            write(writer, "BAccountTermStartDate", formatDate(getBAccountTermStartDate()));
        }
        if (StringUtils.isNotEmpty(getBAccountTermEndDate())) {
            write(writer, "BAccountTermEndDate", formatDate(getBAccountTermEndDate()));
        }
        write(writer, "GrossWriteBacks", getGrossWriteBacks());
        write(writer, "NetWriteBacks", getNetWriteBacks());
        write(writer, "VatWriteBacks", getVatWriteBacks());
        write(writer, "GrossMisc", getGrossMisc());
        write(writer, "NetMisc", getNetMisc());
        write(writer, "VatMisc", getVatMisc());
        write(writer, "GrossItem", getGrossItem());
        write(writer, "NetItem", getNetItem());
        write(writer, "VatItem", getVatItem());
        write(writer, "VatTotal", getVatTotal());
        write(writer, "GrossTax", getGrossTax());
        write(writer, "NetTotal", getNetTotal());
        write(writer, "GrossTotal", getGrossTotal());
        write(writer, "GrossSettledAndWriteBacks", getGrossSettledAndWriteBacks());
        write(writer, "NetSettledAndWriteBacks", getNetSettledAndWriteBacks());
        write(writer, "VatSettledAndWriteBacks", getVatSettledAndWriteBacks());
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        write(writer, "NetWithTax", getNetWithTax());
        write(writer, "GrossWithTax", getGrossWithTax());
        write(writer, "NetNoTax", getNetNoTax());
        if (StringUtils.isNotEmpty(getOutstandingDebt())) {
            write(writer, "OutstandingDebt", getOutstandingDebt());
        }
        writer.writeEndElement();
    }
}