//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.12 at 05:23:10 PM IST 
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
 *         &lt;element ref="{}Ldc1"/&gt;
 *         &lt;element ref="{}Supplier1"/&gt;
 *         &lt;element ref="{}Description"/&gt;
 *         &lt;element ref="{}MeterId"/&gt;
 *         &lt;element ref="{}ObjectId"/&gt;
 *         &lt;element ref="{}AnnualConsumption"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="MessageVersion" type="{http://www.w3.org/2001/XMLSchema}float" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ldc1",
    "supplier1",
    "description",
    "meterId",
    "objectId",
    "annualConsumption",
    "meterLocation"
})
@XmlRootElement(name = "EnergyHeader")
public class EnergyHeader {

    @XmlElement(name = "Ldc1", required = true)
    protected String ldc1;
    @XmlElement(name = "Supplier1", required = true)
    protected String supplier1;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "MeterId")
    protected String meterId;
    @XmlElement(name = "ObjectId")
    protected long objectId;
    @XmlElement(name = "AnnualConsumption", required = true)
    protected String annualConsumption;
    @XmlAttribute(name = "MessageVersion")
    protected Float messageVersion;
    @XmlElement(name = "MeterLocation")
    protected String meterLocation;

    /**
     * Gets the value of the ldc1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLdc1() {
        return ldc1;
    }

    /**
     * Sets the value of the ldc1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLdc1(String value) {
        this.ldc1 = value;
    }

    /**
     * Gets the value of the supplier1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupplier1() {
        return supplier1;
    }

    /**
     * Sets the value of the supplier1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupplier1(String value) {
        this.supplier1 = value;
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
    public String getMeterId() {
        return meterId;
    }

    /**
     * Sets the value of the meterId property.
     * 
     */
    public void setMeterId(String value) {
        this.meterId = value;
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
     * Gets the value of the annualConsumption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnnualConsumption() {
        return annualConsumption;
    }

    /**
     * Sets the value of the annualConsumption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnnualConsumption(String value) {
        this.annualConsumption = value;
    }

    /**
     * Gets the value of the messageVersion property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getMessageVersion() {
        return messageVersion;
    }

    /**
     * Sets the value of the messageVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setMessageVersion(Float value) {
        this.messageVersion = value;
    }

    public String getMeterLocation() {
        return meterLocation;
    }

    public void setMeterLocation(String meterLocation) {
        this.meterLocation = meterLocation;
    }
}
