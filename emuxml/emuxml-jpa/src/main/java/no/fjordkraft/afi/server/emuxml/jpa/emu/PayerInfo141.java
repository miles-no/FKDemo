package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class PayerInfo141 extends BaseEmuParser implements EmuParser {

    public static final String PAYERINFO_141 = "PayerInfo-141";

    String Payer1;
    String Payer2;
    String Address1;
    String Address2;
    String PostalCode;
    String City;
    String CountryCode;
    String Country;
    String AccountNo;
    String CountryIsoCode141;
    String Address1New;
    String Address2New;
    String BIC;
    String IBAN;
    String InvoiceLabel;
    String PersIdNo_CoRegNo;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setPayer1(getField("Payer1", streamReader, PAYERINFO_141));
        setPayer2(getField("Payer2", streamReader, PAYERINFO_141));
        setAddress1(getField("Address1", streamReader, PAYERINFO_141));
        setAddress2(getField("Address2", streamReader, PAYERINFO_141));
        setPostalCode(getField("PostalCode", streamReader, PAYERINFO_141));
        setCity(getField("City", streamReader, PAYERINFO_141));
        setCountryCode(getField("CountryCode", streamReader, PAYERINFO_141));
        setCountry(getField("Country", streamReader, PAYERINFO_141));
        setAccountNo(getField("AccountNo", streamReader, PAYERINFO_141));
        setCountryIsoCode141(getField("CountryIsoCode141", streamReader, PAYERINFO_141));
//        setAddress1New(getFieldNull("Address1New", streamReader, PAYERINFO_141));
//        setAddress2New(getFieldNull("Address2New", streamReader, PAYERINFO_141));
//        setBIC(getFieldNull("BIC", streamReader, PAYERINFO_141));
//        setIBAN(getFieldNull("IBAN", streamReader, PAYERINFO_141));
//        setInvoiceLabel(getFieldNull("InvoiceLabel", streamReader, PAYERINFO_141));
//        setPersIdNo_CoRegNo(getFieldNull("PersIdNo-CoRegNo", streamReader, PAYERINFO_141));
        checkEnded(PAYERINFO_141, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        if (StringUtils.isNotEmpty(getPayer1())) {
            writer.writeStartElement(PAYERINFO_141);
            writeCDataEx(writer, "Payer1", getPayer1());
            writeCDataEx(writer, "Payer2", getPayer2());
            writeCDataEx(writer, "Address1", getAddress1());
            writeCDataEx(writer, "Address2", getAddress2());
            writeCDataEx(writer, "PostalCode", getPostalCode());
            writeCDataEx(writer, "City", getCity());
            writeCDataEx(writer, "CountryCode", getCountryCode());
            writeCDataEx(writer, "Country", getCountry());
            writeCDataEx(writer, "AccountNo", getAccountNo());
            writeCDataEx(writer, "CountryIsoCode141", getCountryIsoCode141());
            if (StringUtils.isNoneEmpty(getAddress1New())) {
                writeCDataEx(writer, "Address1New", getAddress1New());
            }
            if (StringUtils.isNoneEmpty(getAddress2New())) {
                writeCDataEx(writer, "Address2New", getAddress2New());
            }
            if (StringUtils.isNoneEmpty(getBIC())) {
                writeCDataEx(writer, "BIC", getBIC());
            }
            if (StringUtils.isNoneEmpty(getIBAN())) {
                writeCDataEx(writer, "IBAN", getIBAN());
            }
            if (StringUtils.isNoneEmpty(getInvoiceLabel())) {
                writeCDataEx(writer, "InvoiceLabel", getInvoiceLabel()); // Fakturamerke
            }
            if (StringUtils.isNoneEmpty(getPersIdNo_CoRegNo())) {
                writeCDataEx(writer, "PersIdNo-CoRegNo", getPersIdNo_CoRegNo());
            }
            writer.writeEndElement();
        }
    }
}