//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.12 at 05:23:10 PM IST 
//


package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
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
 *         &lt;element ref="{}DueDate"/&gt;
 *         &lt;element ref="{}Currency"/&gt;
 *         &lt;element ref="{}KidNumber"/&gt;
 *         &lt;element ref="{}PaymentTerms"/&gt;
 *         &lt;element ref="{}OverDuePercent"/&gt;
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
    "dueDate",
    "currency",
    "kidNumber",
    "paymentTerms",
    "overDuePercent"
})
@XmlRootElement(name = "Payment")
public class Payment {

    @XmlElement(name = "DueDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dueDate;
    @XmlElement(name = "Currency", required = true)
    protected String currency;
    @XmlElement(name = "KidNumber")
    protected long kidNumber;
    @XmlElement(name = "PaymentTerms")
    protected float paymentTerms;
    @XmlElement(name = "OverDuePercent")
    protected float overDuePercent;

    /**
     * Gets the value of the dueDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDueDate() {
        return dueDate;
    }

    /**
     * Sets the value of the dueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setDueDate(XMLGregorianCalendar value) {
        this.dueDate = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the kidNumber property.
     * 
     */
    public long getKidNumber() {
        return kidNumber;
    }

    /**
     * Sets the value of the kidNumber property.
     * 
     */
    public void setKidNumber(long value) {
        this.kidNumber = value;
    }

    /**
     * Gets the value of the paymentTerms property.
     * 
     */
    public float getPaymentTerms() {
        return paymentTerms;
    }

    /**
     * Sets the value of the paymentTerms property.
     * 
     */
    public void setPaymentTerms(float value) {
        this.paymentTerms = value;
    }

    /**
     * Gets the value of the overDuePercent property.
     * 
     */
    public float getOverDuePercent() {
        return overDuePercent;
    }

    /**
     * Sets the value of the overDuePercent property.
     * 
     */
    public void setOverDuePercent(float value) {
        this.overDuePercent = value;
    }

}
