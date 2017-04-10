package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class SupplyPointInfo117 extends BaseEmuParser implements EmuParser {

    public static final String SUPPLYPOINTINFO_117 = "SupplyPointInfo-117";

    String SupplyPointId;
    String InstlnId;
    String StreetNo;
    String Description;
    String MeterId;
    String ActivityType;
    String MeterLocationId;
    String ObjectId;
    String GridArea;
    String Address1;
    String Address2;
    String Address3;
    String Address4;
    String Address5;
    String MeterCode;
    String InstSign;
    String HousingCooperative;
    String ServiceAgreeSerialNo;
    String JunctionId;
    String JunctionDescription;
    String readingMateriel;
    String MeterIdNew;
    String PostalCode;
    String City;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setSupplyPointId(getField("SupplyPointId", streamReader, SUPPLYPOINTINFO_117));
        setInstlnId(getField("InstlnId", streamReader, SUPPLYPOINTINFO_117));
        setStreetNo(getField("StreetNo", streamReader, SUPPLYPOINTINFO_117));
        setDescription(getField("Description", streamReader, SUPPLYPOINTINFO_117));
        setMeterId(getField("MeterId", streamReader, SUPPLYPOINTINFO_117));
        setActivityType(getField("ActivityType", streamReader, SUPPLYPOINTINFO_117));
        setMeterLocationId(getField("MeterLocationId", streamReader, SUPPLYPOINTINFO_117));
        setObjectId(getField("ObjectId", streamReader, SUPPLYPOINTINFO_117));
        setGridArea(getField("GridArea", streamReader, SUPPLYPOINTINFO_117));
        setAddress1(getField("Address1", streamReader, SUPPLYPOINTINFO_117));
        setAddress2(getField("Address2", streamReader, SUPPLYPOINTINFO_117));
        setAddress3(getField("Address3", streamReader, SUPPLYPOINTINFO_117));
        setAddress4(getField("Address4", streamReader, SUPPLYPOINTINFO_117));
        setAddress5(getField("Address5", streamReader, SUPPLYPOINTINFO_117));
        setMeterCode(getField("MeterCode", streamReader, SUPPLYPOINTINFO_117));
        setInstSign(getField("InstSign", streamReader, SUPPLYPOINTINFO_117));
        setHousingCooperative(getField("HousingCooperative", streamReader, SUPPLYPOINTINFO_117));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, SUPPLYPOINTINFO_117));
        setJunctionId(getField("JunctionId", streamReader, SUPPLYPOINTINFO_117));
        setJunctionDescription(getField("JunctionDescription", streamReader, SUPPLYPOINTINFO_117));
        setReadingMateriel(getField("readingMateriel", streamReader, SUPPLYPOINTINFO_117));
        setMeterIdNew(getField("MeterIdNew", streamReader, SUPPLYPOINTINFO_117));
//        setPostalCode(getFieldNull("PostalCode", streamReader, SUPPLYPOINTINFO_117));
//        setCity(getFieldNull("City", streamReader, SUPPLYPOINTINFO_117));
        checkEnded(SUPPLYPOINTINFO_117, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(SUPPLYPOINTINFO_117);
        write(writer, "SupplyPointId", getSupplyPointId());
        write(writer, "InstlnId", getInstlnId());
        writeCDataEx(writer, "StreetNo", getStreetNo());
        writeCDataEx(writer, "Description", getDescription());
        writeCDataEx(writer, "MeterId", getMeterId());
        writeCDataEx(writer, "ActivityType", getActivityType());
        write(writer, "MeterLocationId", getMeterLocationId());
        writeCDataEx(writer, "ObjectId", getObjectId());
        write(writer, "GridArea", getGridArea());
        writeCDataEx(writer, "Address1", getAddress1());
        writeCDataEx(writer, "Address2", getAddress2());
        writeCDataEx(writer, "Address3", getAddress3());
        writeCDataEx(writer, "Address4", getAddress4());
        writeCDataEx(writer, "Address5", getAddress5());
        write(writer, "MeterCode", getMeterCode());
        write(writer, "InstSign", getInstSign());
        write(writer, "HousingCooperative", getHousingCooperative());
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        write(writer, "JunctionId", getJunctionId());
        write(writer, "JunctionDescription", getJunctionDescription());
        write(writer, "readingMateriel", getReadingMateriel());
        writeCDataEx(writer, "MeterIdNew", getMeterIdNew());
        if (StringUtils.isNotEmpty(getPostalCode())) {
            writeCDataEx(writer, "PostalCode", getPostalCode());
        }
        if (StringUtils.isNotEmpty(getCity())) {
            writeCDataEx(writer, "City", getCity());
        }
        writer.writeEndElement();
    }
}
