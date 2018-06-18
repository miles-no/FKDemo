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
 *         &lt;element ref="{}StartDate"/&gt;
 *         &lt;element ref="{}EndDate"/&gt;
 *         &lt;element ref="{}MeterId"/&gt;
 *         &lt;element ref="{}StartReading"/&gt;
 *         &lt;element ref="{}FinalC-Reading"/&gt;
 *         &lt;element ref="{}AreaFactor"/&gt;
 *         &lt;element ref="{}FixedPricePeriodStart"/&gt;
 *         &lt;element ref="{}FixedPricePeriodEnd"/&gt;
 *         &lt;element ref="{}TotalDigits"/&gt;
 *         &lt;element ref="{}TotalDecimals"/&gt;
 *         &lt;element ref="{}Method"/&gt;
 *         &lt;element ref="{}MethodStartReading"/&gt;
 *         &lt;element ref="{}MethodFinalC-Reading"/&gt;
 *         &lt;element ref="{}V-Reading"/&gt;
 *         &lt;element ref="{}ProdCounterNo"/&gt;
 *         &lt;element ref="{}MeterCounterNo"/&gt;
 *         &lt;element ref="{}Denomination"/&gt;
 *         &lt;element ref="{}TotalDays"/&gt;
 *         &lt;element ref="{}Description"/&gt;
 *         &lt;element ref="{}FixedPricePeriodDays"/&gt;
 *         &lt;element ref="{}ServiceAgreeSerialNo"/&gt;
 *         &lt;element ref="{}TotConsCurrCounter"/&gt;
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
    "startDate",
    "endDate",
    "meterId",
    "startReading",
    "finalCReading",
    "areaFactor",
    "fixedPricePeriodStart",
    "fixedPricePeriodEnd",
    "totalDigits",
    "totalDecimals",
    "method",
    "methodStartReading",
    "methodFinalCReading",
    "vReading",
    "prodCounterNo",
    "meterCounterNo",
    "denomination",
    "totalDays",
    "description",
    "fixedPricePeriodDays",
    "serviceAgreeSerialNo",
    "totConsCurrCounter",
    "meterIdNew"
})
@XmlRootElement(name = "ReadingInfo-111")
public class ReadingInfo111 {

    @XmlElement(name = "StartDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDate;
    @XmlElement(name = "EndDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;
    @XmlElement(name = "MeterId")
    protected String meterId;
    @XmlElement(name = "StartReading")
    protected double startReading;
    @XmlElement(name = "FinalC-Reading")
    protected double finalCReading;
    @XmlElement(name = "AreaFactor")
    protected double areaFactor;
    @XmlElement(name = "FixedPricePeriodStart", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fixedPricePeriodStart;
    @XmlElement(name = "FixedPricePeriodEnd", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fixedPricePeriodEnd;
    @XmlElement(name = "TotalDigits")
    protected byte totalDigits;
    @XmlElement(name = "TotalDecimals")
    protected byte totalDecimals;
    @XmlElement(name = "Method")
    protected byte method;
    @XmlElement(name = "MethodStartReading")
    protected byte methodStartReading;
    @XmlElement(name = "MethodFinalC-Reading")
    protected double methodFinalCReading;
    @XmlElement(name = "V-Reading", required = true)
    protected String vReading;
    @XmlElement(name = "ProdCounterNo")
    protected byte prodCounterNo;
    @XmlElement(name = "MeterCounterNo")
    protected byte meterCounterNo;
    @XmlElement(name = "Denomination", required = true)
    protected String denomination;
    @XmlElement(name = "TotalDays")
    protected byte totalDays;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "FixedPricePeriodDays")
    protected byte fixedPricePeriodDays;
    @XmlElement(name = "ServiceAgreeSerialNo")
    protected byte serviceAgreeSerialNo;
    @XmlElement(name = "TotConsCurrCounter")
    protected double totConsCurrCounter;
    @XmlElement(name = "MeterIdNew")
    protected long meterIdNew;

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
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
     * Gets the value of the startReading property.
     *
     */
    public double getStartReading() {
        return startReading;
    }

    /**
     * Sets the value of the startReading property.
     *
     */
    public void setStartReading(double value) {
        this.startReading = value;
    }

    /**
     * Gets the value of the finalCReading property.
     *
     */
    public double getFinalCReading() {
        return finalCReading;
    }

    /**
     * Sets the value of the finalCReading property.
     *
     */
    public void setFinalCReading(double value) {
        this.finalCReading = value;
    }

    /**
     * Gets the value of the areaFactor property.
     *
     */
    public double getAreaFactor() {
        return areaFactor;
    }

    /**
     * Sets the value of the areaFactor property.
     *
     */
    public void setAreaFactor(double value) {
        this.areaFactor = value;
    }

    /**
     * Gets the value of the fixedPricePeriodStart property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getFixedPricePeriodStart() {
        return fixedPricePeriodStart;
    }

    /**
     * Sets the value of the fixedPricePeriodStart property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setFixedPricePeriodStart(XMLGregorianCalendar value) {
        this.fixedPricePeriodStart = value;
    }

    /**
     * Gets the value of the fixedPricePeriodEnd property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getFixedPricePeriodEnd() {
        return fixedPricePeriodEnd;
    }

    /**
     * Sets the value of the fixedPricePeriodEnd property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFixedPricePeriodEnd(XMLGregorianCalendar value) {
        this.fixedPricePeriodEnd = value;
    }

    /**
     * Gets the value of the totalDigits property.
     * 
     */
    public byte getTotalDigits() {
        return totalDigits;
    }

    /**
     * Sets the value of the totalDigits property.
     * 
     */
    public void setTotalDigits(byte value) {
        this.totalDigits = value;
    }

    /**
     * Gets the value of the totalDecimals property.
     * 
     */
    public byte getTotalDecimals() {
        return totalDecimals;
    }

    /**
     * Sets the value of the totalDecimals property.
     * 
     */
    public void setTotalDecimals(byte value) {
        this.totalDecimals = value;
    }

    /**
     * Gets the value of the method property.
     * 
     */
    public byte getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     */
    public void setMethod(byte value) {
        this.method = value;
    }

    /**
     * Gets the value of the methodStartReading property.
     * 
     */
    public byte getMethodStartReading() {
        return methodStartReading;
    }

    /**
     * Sets the value of the methodStartReading property.
     * 
     */
    public void setMethodStartReading(byte value) {
        this.methodStartReading = value;
    }

    /**
     * Gets the value of the methodFinalCReading property.
     * 
     */
    public double getMethodFinalCReading() {
        return methodFinalCReading;
    }

    /**
     * Sets the value of the methodFinalCReading property.
     * 
     */
    public void setMethodFinalCReading(double value) {
        this.methodFinalCReading = value;
    }

    /**
     * Gets the value of the vReading property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVReading() {
        return vReading;
    }

    /**
     * Sets the value of the vReading property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVReading(String value) {
        this.vReading = value;
    }

    /**
     * Gets the value of the prodCounterNo property.
     * 
     */
    public byte getProdCounterNo() {
        return prodCounterNo;
    }

    /**
     * Sets the value of the prodCounterNo property.
     * 
     */
    public void setProdCounterNo(byte value) {
        this.prodCounterNo = value;
    }

    /**
     * Gets the value of the meterCounterNo property.
     * 
     */
    public byte getMeterCounterNo() {
        return meterCounterNo;
    }

    /**
     * Sets the value of the meterCounterNo property.
     * 
     */
    public void setMeterCounterNo(byte value) {
        this.meterCounterNo = value;
    }

    /**
     * Gets the value of the denomination property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenomination() {
        return denomination;
    }

    /**
     * Sets the value of the denomination property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenomination(String value) {
        this.denomination = value;
    }

    /**
     * Gets the value of the totalDays property.
     * 
     */
    public byte getTotalDays() {
        return totalDays;
    }

    /**
     * Sets the value of the totalDays property.
     * 
     */
    public void setTotalDays(byte value) {
        this.totalDays = value;
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
     * Gets the value of the fixedPricePeriodDays property.
     * 
     */
    public byte getFixedPricePeriodDays() {
        return fixedPricePeriodDays;
    }

    /**
     * Sets the value of the fixedPricePeriodDays property.
     * 
     */
    public void setFixedPricePeriodDays(byte value) {
        this.fixedPricePeriodDays = value;
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
     * Gets the value of the totConsCurrCounter property.
     * 
     */
    public double getTotConsCurrCounter() {
        return totConsCurrCounter;
    }

    /**
     * Sets the value of the totConsCurrCounter property.
     * 
     */
    public void setTotConsCurrCounter(double value) {
        this.totConsCurrCounter = value;
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
