//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.02 at 07:13:02 PM IST 
//


package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{}VatRate"/&gt;
 *         &lt;element ref="{}SumBasis"/&gt;
 *         &lt;element ref="{}SumVat"/&gt;
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
    "vatRate",
    "sumBasis",
    "sumVat",
    "serviceAgreeSerialNo"
})
@XmlRootElement(name = "VatSpecInvoiceOrder-114")
public class VatSpecInvoiceOrder114 {

    @XmlElement(name = "VatRate")
    protected float vatRate;
    @XmlElement(name = "SumBasis")
    protected float sumBasis;
    @XmlElement(name = "SumVat")
    protected float sumVat;
    @XmlElement(name = "ServiceAgreeSerialNo")
    protected byte serviceAgreeSerialNo;

    /**
     * Gets the value of the vatRate property.
     * 
     */
    public float getVatRate() {
        return vatRate;
    }

    /**
     * Sets the value of the vatRate property.
     * 
     */
    public void setVatRate(float value) {
        this.vatRate = value;
    }

    /**
     * Gets the value of the sumBasis property.
     * 
     */
    public float getSumBasis() {
        return sumBasis;
    }

    /**
     * Sets the value of the sumBasis property.
     * 
     */
    public void setSumBasis(float value) {
        this.sumBasis = value;
    }

    /**
     * Gets the value of the sumVat property.
     * 
     */
    public float getSumVat() {
        return sumVat;
    }

    /**
     * Sets the value of the sumVat property.
     * 
     */
    public void setSumVat(float value) {
        this.sumVat = value;
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
