//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.02 at 07:13:02 PM IST 
//


package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Map;


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
 *         &lt;element ref="{}TransactionId"/&gt;
 *         &lt;element ref="{}TransactionSequence"/&gt;
 *         &lt;element ref="{}ParentTransactionId"/&gt;
 *         &lt;element ref="{}TransactionType"/&gt;
 *         &lt;element ref="{}TransactionCategory"/&gt;
 *         &lt;element ref="{}TransactionDate"/&gt;
 *         &lt;element ref="{}ValueDate"/&gt;
 *         &lt;element ref="{}Amount"/&gt;
 *         &lt;element ref="{}VatAmount"/&gt;
 *         &lt;element ref="{}AmountWithVat"/&gt;
 *         &lt;element ref="{}Reference"/&gt;
 *         &lt;element ref="{}Url"/&gt;
 *         &lt;element ref="{}FreeText"/&gt;
 *         &lt;element ref="{}Distributions"/&gt;
 *         &lt;element ref="{}CollectionFeeForStatements"/&gt;
 *         &lt;element ref="{}StartDate"/&gt;
 *         &lt;element ref="{}EndDate"/&gt;
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
    "transactionId",
    "transactionSequence",
    "parentTransactionId",
    "transactionType",
    "transactionCategory",
    "transactionDate",
    "valueDate",
    "amount",
    "vatAmount",
    "amountWithVat",
    "reference",
    "url",
    "freeText",
    "distributions",
    "collectionFeeForStatements",
    "startDate",
    "endDate",
    "maalepunktID",
    "leveringAddress",
    "vatRate",
    "displayTransaction"
})
@XmlRootElement(name = "Transaction")
public class Transaction {

    @XmlElement(name = "TransactionId")
    protected int transactionId;
    @XmlElement(name = "TransactionSequence")
    protected int transactionSequence;
    @XmlElement(name = "ParentTransactionId", required = true)
    protected String parentTransactionId;
    @XmlElement(name = "TransactionType", required = true)
    protected String transactionType;
    @XmlElement(name = "TransactionCategory", required = true)
    protected String transactionCategory;
    @XmlElement(name = "TransactionDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar transactionDate;
    @XmlElement(name = "ValueDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar valueDate;
    @XmlElement(name = "Amount")
    protected double amount;
    @XmlElement(name = "VatAmount")
    protected double vatAmount;
    @XmlElement(name = "AmountWithVat")
    protected double amountWithVat;
    @XmlElement(name = "Reference", required = true)
    protected String reference;
    @XmlElement(name = "Url", required = true)
    protected String url;
    @XmlElement(name = "FreeText", required = true)
    protected String freeText;
    @XmlElement(name = "Distributions", required = true)
    protected Distributions distributions;
    @XmlElement(name = "CollectionFeeForStatements", required = true)
    protected String collectionFeeForStatements;
    @XmlElement(name = "StartDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDate;
    @XmlElement(name = "EndDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;

    @XmlElement(name = "MAALEPUNKT")
    @XmlSchemaType(name="")
    protected long maalepunktID;

    @XmlElement(name = "LeveringAddress")
    @XmlSchemaType(name = "string")
    protected String leveringAddress;

    @XmlElement(name = "VatRate")
    protected String vatRate;

    @XmlElement(name = "DisplayTransaction")
    protected Boolean displayTransaction = true;

    @XmlTransient
    private String transactionName;

    @XmlTransient
    private Map<Double,Double> mapOfVatVsAmount;


    /**
     * Gets the value of the transactionId property.
     * 
     */
    public int getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the transactionId property.
     * 
     */
    public void setTransactionId(int value) {
        this.transactionId = value;
    }

    /**
     * Gets the value of the parentTransactionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentTransactionId() {
        return parentTransactionId;
    }

    /**
     * Sets the value of the parentTransactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentTransactionId(String value) {
        this.parentTransactionId = value;
    }

    /**
     * Gets the value of the transactionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the value of the transactionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionType(String value) {
        this.transactionType = value;
    }

    /**
     * Gets the value of the transactionCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionCategory() {
        return transactionCategory;
    }

    /**
     * Sets the value of the transactionCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionCategory(String value) {
        this.transactionCategory = value;
    }

    /**
     * Gets the value of the transactionDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getTransactionDate() {
        return transactionDate;
    }

    /**
     * Sets the value of the transactionDate property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setTransactionDate(XMLGregorianCalendar value) {
        this.transactionDate = value;
    }

    /**
     * Gets the value of the valueDate property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getValueDate() {
        return valueDate;
    }

    /**
     * Sets the value of the valueDate property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValueDate(XMLGregorianCalendar value) {
        this.valueDate = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     */
    public void setAmount(double value) {
        this.amount = value;
    }

    /**
     * Gets the value of the vatAmount property.
     * 
     */
    public double getVatAmount() {
        return vatAmount;
    }

    /**
     * Sets the value of the vatAmount property.
     * 
     */
    public void setVatAmount(double value) {
        this.vatAmount = value;
    }

    /**
     * Gets the value of the amountWithVat property.
     * 
     */
    public double getAmountWithVat() {
        return amountWithVat;
    }

    /**
     * Sets the value of the amountWithVat property.
     * 
     */
    public void setAmountWithVat(double value) {
        this.amountWithVat = value;
    }

    /**
     * Gets the value of the reference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReference(String value) {
        this.reference = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the freeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFreeText() {
        return freeText;
    }

    /**
     * Sets the value of the freeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFreeText(String value) {
        this.freeText = value;
    }

    /**
     * Gets the value of the distributions property.
     * 
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.Distributions }
     *     
     */
    public Distributions getDistributions() {
        return distributions;
    }

    /**
     * Sets the value of the distributions property.
     * 
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.Distributions }
     *     
     */
    public void setDistributions(Distributions value) {
        this.distributions = value;
    }

    /**
     * Gets the value of the collectionFeeForStatements property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollectionFeeForStatements() {
        return collectionFeeForStatements;
    }

    /**
     * Sets the value of the collectionFeeForStatements property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollectionFeeForStatements(String value) {
        this.collectionFeeForStatements = value;
    }

    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    public void setStartDate(XMLGregorianCalendar startDate) {
        this.startDate = startDate;
    }

    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    public void setEndDate(XMLGregorianCalendar endDate) {
        this.endDate = endDate;
    }

    public int getTransactionSequence() {
        return transactionSequence;
    }

    public void setTransactionSequence(int transactionSequence) {
        this.transactionSequence = transactionSequence;
    }

    public long getMaalepunktID() {
        return maalepunktID;
    }

    public void setMaalepunktID(long maalepunktID) {
        this.maalepunktID = maalepunktID;
    }

    public String getLeveringAddress() {
        return leveringAddress;
    }

    public void setLeveringAddress(String leveringAddress) {
        this.leveringAddress = leveringAddress;
    }

    public String getVatRate() {
        return vatRate;
    }

    public void setVatRate(String vatRate) {
        this.vatRate = vatRate;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Map<Double, Double> getMapOfVatVsAmount() {
        return mapOfVatVsAmount;
    }

    public void setMapOfVatVsAmount(Map<Double, Double> mapOfVatVsAmount) {
        this.mapOfVatVsAmount = mapOfVatVsAmount;
    }

    public Double getAmountBasedOnVat(Double vat) {
        return mapOfVatVsAmount.get(vat);
    }

    public boolean isDisplayTransaction() {
        return displayTransaction;
    }

    public void setDisplayTransaction(boolean displayTransaction) {
        displayTransaction = displayTransaction;
    }
}
