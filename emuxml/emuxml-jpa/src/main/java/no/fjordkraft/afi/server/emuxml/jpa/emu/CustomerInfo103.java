package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class CustomerInfo103 extends BaseEmuParser implements EmuParser {

    public static final String CUSTOMERINFO_103 = "CustomerInfo-103";

    String Name;
    String Name2;
    String Address1;
    String Address2;
    String PostalCode;
    String City;
    String CountryCode;
    String Country;
    String Address3;
    String Address4;
    String Address5;
    String PersIdNoCoRegNo;
    String PhoneNo;
    String MobilePhoneNo;
    String EmailAddress;
    String CountryIsoCode103;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setName(getField("Name", streamReader, CUSTOMERINFO_103));
        setName2(getField("Name2", streamReader, CUSTOMERINFO_103));
        setAddress1(getField("Address1", streamReader, CUSTOMERINFO_103));
        setAddress2(getField("Address2", streamReader, CUSTOMERINFO_103));
        setPostalCode(getField("PostalCode", streamReader, CUSTOMERINFO_103));
        setCity(getField("City", streamReader, CUSTOMERINFO_103));
        setCountryCode(getField("CountryCode", streamReader, CUSTOMERINFO_103));
        setCountry(getField("Country", streamReader, CUSTOMERINFO_103));
        setAddress3(getField("Address3", streamReader, CUSTOMERINFO_103));
        setAddress4(getField("Address4", streamReader, CUSTOMERINFO_103));
        setAddress5(getField("Address5", streamReader, CUSTOMERINFO_103));
        setPersIdNoCoRegNo(getField("PersIdNo-CoRegNo", streamReader, CUSTOMERINFO_103));
        setPhoneNo(getField("PhoneNo", streamReader, CUSTOMERINFO_103));
        setMobilePhoneNo(getField("MobilePhoneNo", streamReader, CUSTOMERINFO_103));
        setEmailAddress(getField("EmailAddress", streamReader, CUSTOMERINFO_103));
        setCountryIsoCode103(getField("CountryIsoCode103", streamReader, CUSTOMERINFO_103));
        checkEnded(CUSTOMERINFO_103, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(CUSTOMERINFO_103);
        writeCDataEx(writer, "Name", getName());
        writeCDataEx(writer, "Name2", getName2());
        writeCDataEx(writer, "Address1", getAddress1());
        writeCDataEx(writer, "Address2", getAddress2());
        writeCDataEx(writer, "PostalCode", getPostalCode());
        writeCDataEx(writer, "City", getCity());
        writeCDataEx(writer, "CountryCode", getCountryCode());
        writeCDataEx(writer, "Country", getCountry());
        writeCDataEx(writer, "Address3", getAddress3());
        writeCDataEx(writer, "Address4", getAddress4());
        writeCDataEx(writer, "Address5", getAddress5());
        writeCDataEx(writer, "PersIdNo-CoRegNo", getPersIdNoCoRegNo());
        writeCDataEx(writer, "PhoneNo", getPhoneNo());
        writeCDataEx(writer, "MobilePhoneNo", getMobilePhoneNo());
        writeCDataEx(writer, "EmailAddress", getEmailAddress());
        writeCDataEx(writer, "CountryIsoCode103", getCountryIsoCode103());
        writer.writeEndElement();
    }
}