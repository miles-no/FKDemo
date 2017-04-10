package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class UtilityInfo001 extends BaseEmuParser implements EmuParser {

    public static final String UTILITYINFO_001 = "UtilityInfo-001";

    String User;
    String Utility;
    String Address;
    String PostalCode;
    String City;
    String AccountNo;
    String FormatVersion;
    String CompanyRegNo;
    String PrintFormat;
    String E_InvoiceStatus;
    String ProductionDate;
    String ProductionStartTime;
    String KisSystemId;
    String PrintCode;
    String Type;
    String BIC_Supplier;
    String IBAN_Supplier;
    String BIC_Customer;
    String IBAN_Customer;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setUser(getField("User", streamReader, UTILITYINFO_001));
        setUtility(getField("Utility", streamReader, UTILITYINFO_001));
        setAddress(getField("Address", streamReader, UTILITYINFO_001));
        setPostalCode(getField("PostalCode", streamReader, UTILITYINFO_001));
        setCity(getField("City", streamReader, UTILITYINFO_001));
        setAccountNo(getField("AccountNo", streamReader, UTILITYINFO_001));
        setFormatVersion(getField("FormatVersion", streamReader, UTILITYINFO_001));
        setCompanyRegNo(getField("CompanyRegNo", streamReader, UTILITYINFO_001));
        setPrintFormat(getField("PrintFormat", streamReader, UTILITYINFO_001));
        setE_InvoiceStatus(getField("E-InvoiceStatus", streamReader, UTILITYINFO_001));
        setProductionDate(getField("ProductionDate", streamReader, UTILITYINFO_001));
        setProductionStartTime(getField("ProductionStartTime", streamReader, UTILITYINFO_001));
        setKisSystemId(getField("KisSystemId", streamReader, UTILITYINFO_001));
        setPrintCode(getField("PrintCode", streamReader, UTILITYINFO_001));
        setType(getField("Type", streamReader, UTILITYINFO_001));
        setBIC_Supplier(getField("BIC-Supplier", streamReader, UTILITYINFO_001));
        setIBAN_Supplier(getField("IBAN-Supplier", streamReader, UTILITYINFO_001));
        setBIC_Customer(getField("BIC-Customer", streamReader, UTILITYINFO_001));
        setIBAN_Customer(getField("IBAN-Customer", streamReader, UTILITYINFO_001));
        checkEnded(UTILITYINFO_001, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
    }

}