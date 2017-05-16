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
 *         &lt;element ref="{}InvAgreementId"/&gt;
 *         &lt;element ref="{}AgreementDate"/&gt;
 *         &lt;element ref="{}IndustryPrivate"/&gt;
 *         &lt;element ref="{}InvoiceLabel"/&gt;
 *         &lt;element ref="{}InterestId"/&gt;
 *         &lt;element ref="{}InterestRate"/&gt;
 *         &lt;element ref="{}PayerStatus"/&gt;
 *         &lt;element ref="{}AccountNo"/&gt;
 *         &lt;element ref="{}TotalPrinted"/&gt;
 *         &lt;element ref="{}TotalPrintedOtherInvoices"/&gt;
 *         &lt;element ref="{}BIC-Customer"/&gt;
 *         &lt;element ref="{}IBAN-Customer"/&gt;
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
    "invAgreementId",
    "agreementDate",
    "industryPrivate",
    "invoiceLabel",
    "interestId",
    "interestRate",
    "payerStatus",
    "accountNo",
    "totalPrinted",
    "totalPrintedOtherInvoices",
    "bicCustomer",
    "ibanCustomer"
})
@XmlRootElement(name = "InvoiceAgreementInfo-105")
public class InvoiceAgreementInfo105 {

    @XmlElement(name = "InvAgreementId")
    protected int invAgreementId;
    @XmlElement(name = "AgreementDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar agreementDate;
    @XmlElement(name = "IndustryPrivate", required = true)
    protected String industryPrivate;
    @XmlElement(name = "InvoiceLabel", required = true)
    protected String invoiceLabel;
    @XmlElement(name = "InterestId", required = true)
    protected String interestId;
    @XmlElement(name = "InterestRate", required = true)
    protected InterestRate interestRate;
    @XmlElement(name = "PayerStatus", required = true)
    protected String payerStatus;
    @XmlElement(name = "AccountNo", required = true)
    protected String accountNo;
    @XmlElement(name = "TotalPrinted")
    protected float totalPrinted;
    @XmlElement(name = "TotalPrintedOtherInvoices")
    protected float totalPrintedOtherInvoices;
    @XmlElement(name = "BIC-Customer", required = true)
    protected String bicCustomer;
    @XmlElement(name = "IBAN-Customer", required = true)
    protected String ibanCustomer;

    /**
     * Gets the value of the invAgreementId property.
     * 
     */
    public int getInvAgreementId() {
        return invAgreementId;
    }

    /**
     * Sets the value of the invAgreementId property.
     * 
     */
    public void setInvAgreementId(int value) {
        this.invAgreementId = value;
    }

    /**
     * Gets the value of the agreementDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAgreementDate() {
        return agreementDate;
    }

    /**
     * Sets the value of the agreementDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setAgreementDate(XMLGregorianCalendar value) {
        this.agreementDate = value;
    }

    /**
     * Gets the value of the industryPrivate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndustryPrivate() {
        return industryPrivate;
    }

    /**
     * Sets the value of the industryPrivate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndustryPrivate(String value) {
        this.industryPrivate = value;
    }

    /**
     * Gets the value of the invoiceLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceLabel() {
        return invoiceLabel;
    }

    /**
     * Sets the value of the invoiceLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceLabel(String value) {
        this.invoiceLabel = value;
    }

    /**
     * Gets the value of the interestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterestId() {
        return interestId;
    }

    /**
     * Sets the value of the interestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterestId(String value) {
        this.interestId = value;
    }

    /**
     * Gets the value of the interestRate property.
     * 
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.InterestRate }
     *     
     */
    public InterestRate getInterestRate() {
        return interestRate;
    }

    /**
     * Sets the value of the interestRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.InterestRate }
     *     
     */
    public void setInterestRate(InterestRate value) {
        this.interestRate = value;
    }

    /**
     * Gets the value of the payerStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayerStatus() {
        return payerStatus;
    }

    /**
     * Sets the value of the payerStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayerStatus(String value) {
        this.payerStatus = value;
    }

    /**
     * Gets the value of the accountNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNo() {
        return accountNo;
    }

    /**
     * Sets the value of the accountNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNo(String value) {
        this.accountNo = value;
    }

    /**
     * Gets the value of the totalPrinted property.
     * 
     */
    public float getTotalPrinted() {
        return totalPrinted;
    }

    /**
     * Sets the value of the totalPrinted property.
     * 
     */
    public void setTotalPrinted(float value) {
        this.totalPrinted = value;
    }

    /**
     * Gets the value of the totalPrintedOtherInvoices property.
     * 
     */
    public float getTotalPrintedOtherInvoices() {
        return totalPrintedOtherInvoices;
    }

    /**
     * Sets the value of the totalPrintedOtherInvoices property.
     * 
     */
    public void setTotalPrintedOtherInvoices(float value) {
        this.totalPrintedOtherInvoices = value;
    }

    /**
     * Gets the value of the bicCustomer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBICCustomer() {
        return bicCustomer;
    }

    /**
     * Sets the value of the bicCustomer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBICCustomer(String value) {
        this.bicCustomer = value;
    }

    /**
     * Gets the value of the ibanCustomer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIBANCustomer() {
        return ibanCustomer;
    }

    /**
     * Sets the value of the ibanCustomer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIBANCustomer(String value) {
        this.ibanCustomer = value;
    }

}
