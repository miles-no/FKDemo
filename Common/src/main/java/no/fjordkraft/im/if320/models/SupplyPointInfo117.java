//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.02 at 07:13:02 PM IST 
//


package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}SupplyPointId"/&gt;
 *         &lt;element ref="{}InstlnId"/&gt;
 *         &lt;element ref="{}StreetNo"/&gt;
 *         &lt;element ref="{}Description"/&gt;
 *         &lt;element ref="{}MeterId"/&gt;
 *         &lt;element ref="{}ActivityType"/&gt;
 *         &lt;element ref="{}MeterLocationId"/&gt;
 *         &lt;element ref="{}ObjectId"/&gt;
 *         &lt;element ref="{}GridArea"/&gt;
 *         &lt;element ref="{}Address1"/&gt;
 *         &lt;element ref="{}Address2"/&gt;
 *         &lt;element ref="{}Address3"/&gt;
 *         &lt;element ref="{}Address4"/&gt;
 *         &lt;element ref="{}Address5"/&gt;
 *         &lt;element ref="{}MeterCode"/&gt;
 *         &lt;element ref="{}InstSign"/&gt;
 *         &lt;element ref="{}HousingCooperative"/&gt;
 *         &lt;element ref="{}ServiceAgreeSerialNo"/&gt;
 *         &lt;element ref="{}JunctionId"/&gt;
 *         &lt;element ref="{}JunctionDescription"/&gt;
 *         &lt;element ref="{}readingMateriel"/&gt;
 *         &lt;element ref="{}MeterIdNew"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "supplyPointId",
    "instlnId",
    "streetNo",
    "description",
    "meterId",
    "activityType",
    "meterLocationId",
    "objectId",
    "gridArea",
    "address1",
    "address2",
    "address3",
    "address4",
    "address5",
    "meterCode",
    "instSign",
    "housingCooperative",
    "serviceAgreeSerialNo",
    "junctionId",
    "junctionDescription",
    "readingMateriel",
    "meterIdNew"
})
@XmlRootElement(name = "SupplyPointInfo-117")
public class SupplyPointInfo117 {

    @XmlElement(name = "SupplyPointId", required = true)
    protected String supplyPointId;
    @XmlElement(name = "InstlnId", required = true)
    protected String instlnId;
    @XmlElement(name = "StreetNo", required = true)
    protected String streetNo;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "MeterId")
    protected long meterId;
    @XmlElement(name = "ActivityType", required = true)
    protected String activityType;
    @XmlElement(name = "MeterLocationId")
    protected byte meterLocationId;
    @XmlElement(name = "ObjectId")
    protected long objectId;
    @XmlElement(name = "GridArea", required = true)
    protected String gridArea;
    @XmlElement(name = "Address1", required = true)
    protected String address1;
    @XmlElement(name = "Address2", required = true)
    protected String address2;
    @XmlElement(name = "Address3", required = true)
    protected String address3;
    @XmlElement(name = "Address4", required = true)
    protected String address4;
    @XmlElement(name = "Address5", required = true)
    protected String address5;
    @XmlElement(name = "MeterCode", required = true)
    protected String meterCode;
    @XmlElement(name = "InstSign", required = true)
    protected String instSign;
    @XmlElement(name = "HousingCooperative", required = true)
    protected String housingCooperative;
    @XmlElement(name = "ServiceAgreeSerialNo")
    protected byte serviceAgreeSerialNo;
    @XmlElement(name = "JunctionId", required = true)
    protected String junctionId;
    @XmlElement(name = "JunctionDescription", required = true)
    protected String junctionDescription;
    @XmlElement(required = true)
    protected String readingMateriel;
    @XmlElement(name = "MeterIdNew")
    protected long meterIdNew;

    /**
     * Gets the value of the supplyPointId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupplyPointId() {
        return supplyPointId;
    }

    /**
     * Sets the value of the supplyPointId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupplyPointId(String value) {
        this.supplyPointId = value;
    }

    /**
     * Gets the value of the instlnId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstlnId() {
        return instlnId;
    }

    /**
     * Sets the value of the instlnId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstlnId(String value) {
        this.instlnId = value;
    }

    /**
     * Gets the value of the streetNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreetNo() {
        return streetNo;
    }

    /**
     * Sets the value of the streetNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreetNo(String value) {
        this.streetNo = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the meterId property.
     * 
     */
    public long getMeterId() {
        return meterId;
    }

    /**
     * Sets the value of the meterId property.
     * 
     */
    public void setMeterId(long value) {
        this.meterId = value;
    }

    /**
     * Gets the value of the activityType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityType() {
        return activityType;
    }

    /**
     * Sets the value of the activityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityType(String value) {
        this.activityType = value;
    }

    /**
     * Gets the value of the meterLocationId property.
     * 
     */
    public byte getMeterLocationId() {
        return meterLocationId;
    }

    /**
     * Sets the value of the meterLocationId property.
     * 
     */
    public void setMeterLocationId(byte value) {
        this.meterLocationId = value;
    }

    /**
     * Gets the value of the objectId property.
     * 
     */
    public long getObjectId() {
        return objectId;
    }

    /**
     * Sets the value of the objectId property.
     * 
     */
    public void setObjectId(long value) {
        this.objectId = value;
    }

    /**
     * Gets the value of the gridArea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGridArea() {
        return gridArea;
    }

    /**
     * Sets the value of the gridArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGridArea(String value) {
        this.gridArea = value;
    }

    /**
     * Gets the value of the address1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * Sets the value of the address1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress1(String value) {
        this.address1 = value;
    }

    /**
     * Gets the value of the address2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * Sets the value of the address2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress2(String value) {
        this.address2 = value;
    }

    /**
     * Gets the value of the address3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress3() {
        return address3;
    }

    /**
     * Sets the value of the address3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress3(String value) {
        this.address3 = value;
    }

    /**
     * Gets the value of the address4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress4() {
        return address4;
    }

    /**
     * Sets the value of the address4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress4(String value) {
        this.address4 = value;
    }

    /**
     * Gets the value of the address5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress5() {
        return address5;
    }

    /**
     * Sets the value of the address5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress5(String value) {
        this.address5 = value;
    }

    /**
     * Gets the value of the meterCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeterCode() {
        return meterCode;
    }

    /**
     * Sets the value of the meterCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeterCode(String value) {
        this.meterCode = value;
    }

    /**
     * Gets the value of the instSign property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstSign() {
        return instSign;
    }

    /**
     * Sets the value of the instSign property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstSign(String value) {
        this.instSign = value;
    }

    /**
     * Gets the value of the housingCooperative property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHousingCooperative() {
        return housingCooperative;
    }

    /**
     * Sets the value of the housingCooperative property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHousingCooperative(String value) {
        this.housingCooperative = value;
    }

    /**
     * Gets the value of the serviceAgreeSerialNo property.
     * 
     */
    public byte getServiceAgreeSerialNo() {
        return serviceAgreeSerialNo;
    }

    /**
     * Sets the value of the serviceAgreeSerialNo property.
     * 
     */
    public void setServiceAgreeSerialNo(byte value) {
        this.serviceAgreeSerialNo = value;
    }

    /**
     * Gets the value of the junctionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJunctionId() {
        return junctionId;
    }

    /**
     * Sets the value of the junctionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJunctionId(String value) {
        this.junctionId = value;
    }

    /**
     * Gets the value of the junctionDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJunctionDescription() {
        return junctionDescription;
    }

    /**
     * Sets the value of the junctionDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJunctionDescription(String value) {
        this.junctionDescription = value;
    }

    /**
     * Gets the value of the readingMateriel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReadingMateriel() {
        return readingMateriel;
    }

    /**
     * Sets the value of the readingMateriel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReadingMateriel(String value) {
        this.readingMateriel = value;
    }

    /**
     * Gets the value of the meterIdNew property.
     * 
     */
    public long getMeterIdNew() {
        return meterIdNew;
    }

    /**
     * Sets the value of the meterIdNew property.
     * 
     */
    public void setMeterIdNew(long value) {
        this.meterIdNew = value;
    }

}
