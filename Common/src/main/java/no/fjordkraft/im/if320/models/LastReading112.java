//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.02 at 07:13:02 PM IST 
//


package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element ref="{}DateRead"/&gt;
 *         &lt;element ref="{}ReadingMethod"/&gt;
 *         &lt;element ref="{}MeterCounter1"/&gt;
 *         &lt;element ref="{}C-Reading1"/&gt;
 *         &lt;element ref="{}MethodText"/&gt;
 *         &lt;element ref="{}ServiceAgreeSerialNo"/&gt;
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
    "dateRead",
    "readingMethod",
    "meterCounter1",
    "cReading1",
    "methodText",
    "serviceAgreeSerialNo"
})
@XmlRootElement(name = "LastReading-112")
public class LastReading112 {

    @XmlElement(name = "DateRead", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateRead;
    @XmlElement(name = "ReadingMethod")
    protected byte readingMethod;
    @XmlElement(name = "MeterCounter1")
    protected byte meterCounter1;
    @XmlElement(name = "C-Reading1")
    protected double cReading1;
    @XmlElement(name = "MethodText", required = true)
    protected String methodText;
    @XmlElement(name = "ServiceAgreeSerialNo")
    protected byte serviceAgreeSerialNo;

    /**
     * Gets the value of the dateRead property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDateRead() {
        return dateRead;
    }

    /**
     * Sets the value of the dateRead property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateRead(XMLGregorianCalendar value) {
        this.dateRead = value;
    }

    /**
     * Gets the value of the readingMethod property.
     * 
     */
    public byte getReadingMethod() {
        return readingMethod;
    }

    /**
     * Sets the value of the readingMethod property.
     * 
     */
    public void setReadingMethod(byte value) {
        this.readingMethod = value;
    }

    /**
     * Gets the value of the meterCounter1 property.
     * 
     */
    public byte getMeterCounter1() {
        return meterCounter1;
    }

    /**
     * Sets the value of the meterCounter1 property.
     * 
     */
    public void setMeterCounter1(byte value) {
        this.meterCounter1 = value;
    }

    /**
     * Gets the value of the cReading1 property.
     * 
     */
    public double getCReading1() {
        return cReading1;
    }

    /**
     * Sets the value of the cReading1 property.
     * 
     */
    public void setCReading1(double value) {
        this.cReading1 = value;
    }

    /**
     * Gets the value of the methodText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethodText() {
        return methodText;
    }

    /**
     * Sets the value of the methodText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethodText(String value) {
        this.methodText = value;
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

}
