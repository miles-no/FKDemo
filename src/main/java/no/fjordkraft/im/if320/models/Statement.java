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
 *         &lt;element ref="{}CustomerReference"/&gt;
 *         &lt;element ref="{}MainSourceReference"/&gt;
 *         &lt;element ref="{}DistributionMethod"/&gt;
 *         &lt;element ref="{}DistributionMethodExternalReference"/&gt;
 *         &lt;element ref="{}VatId"/&gt;
 *         &lt;element ref="{}PaymentType"/&gt;
 *         &lt;element ref="{}PaymentTypeStatus"/&gt;
 *         &lt;element ref="{}EInvoiceId"/&gt;
 *         &lt;element ref="{}NationalId"/&gt;
 *         &lt;element ref="{}Barcode"/&gt;
 *         &lt;element ref="{}Name"/&gt;
 *         &lt;element ref="{}CareOfName"/&gt;
 *         &lt;element ref="{}Address"/&gt;
 *         &lt;element ref="{}Address1"/&gt;
 *         &lt;element ref="{}Address2"/&gt;
 *         &lt;element ref="{}PostCode"/&gt;
 *         &lt;element ref="{}City"/&gt;
 *         &lt;element ref="{}Region"/&gt;
 *         &lt;element ref="{}Country"/&gt;
 *         &lt;element ref="{}Email"/&gt;
 *         &lt;element ref="{}MobileNumber"/&gt;
 *         &lt;element ref="{}StatementDate"/&gt;
 *         &lt;element ref="{}AccountNumber"/&gt;
 *         &lt;element ref="{}SequenceNumber"/&gt;
 *         &lt;element ref="{}StatementOcrNumber"/&gt;
 *         &lt;element ref="{}DueDate"/&gt;
 *         &lt;element ref="{}IngoingBalance"/&gt;
 *         &lt;element ref="{}OutgoingBalance"/&gt;
 *         &lt;element ref="{}CreditLimit"/&gt;
 *         &lt;element ref="{}AvailableCredit"/&gt;
 *         &lt;element ref="{}NonCreditedInterest"/&gt;
 *         &lt;element ref="{}GeneralMessage"/&gt;
 *         &lt;element ref="{}AccountMessage"/&gt;
 *         &lt;element ref="{}CurrentClaim"/&gt;
 *         &lt;element ref="{}TotalOpenClaim"/&gt;
 *         &lt;element ref="{}TotalVatAmount"/&gt;
 *         &lt;element ref="{}CurrentInterestRates"/&gt;
 *         &lt;element ref="{}OutstandingClaims"/&gt;
 *         &lt;element ref="{}Transactions"/&gt;
 *         &lt;element ref="{}LineItems"/&gt;
 *         &lt;element ref="{}InstalmentAccounts"/&gt;
 *         &lt;element ref="{}Attachments"/&gt;
 *         &lt;element ref="{}TaxReport"/&gt;
 *         &lt;element ref="{}DirectDebitAmount"/&gt;
 *         &lt;element ref="{}DirectDebitAgreementDate"/&gt;
 *         &lt;element ref="{}DirectDebitBasetype"/&gt;
 *         &lt;element ref="{}LegalPartClass"/&gt;
 *         &lt;element ref="{}MinClaimFixedAmount"/&gt;
 *         &lt;element ref="{}BudgetStatementAmount"/&gt;
 *         &lt;element ref="{}RecommendedStatementAmount"/&gt;
 *         &lt;element ref="{}Collection"/&gt;
 *         &lt;element ref="{}AccountType"/&gt;
 *         &lt;element ref="{}AccountCategory"/&gt;
 *         &lt;element ref="{}Version"/&gt;
 *         &lt;element ref="{}StatementType"/&gt;
 *         &lt;element ref="{}CreditedInvoice"/&gt;
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
    "customerReference",
    "mainSourceReference",
    "distributionMethod",
    "distributionMethodExternalReference",
    "vatId",
    "paymentType",
    "paymentTypeStatus",
    "eInvoiceId",
    "nationalId",
    "barcode",
    "name",
    "careOfName",
    "address",
    "address1",
    "address2",
    "postCode",
    "city",
    "region",
    "country",
    "email",
    "mobileNumber",
    "statementDate",
    "accountNumber",
    "sequenceNumber",
    "statementOcrNumber",
    "dueDate",
    "ingoingBalance",
    "outgoingBalance",
    "creditLimit",
    "availableCredit",
    "nonCreditedInterest",
    "generalMessage",
    "accountMessage",
    "currentClaim",
    "totalOpenClaim",
    "totalVatAmount",
    "currentInterestRates",
    "outstandingClaims",
    "transactions",
    "lineItems",
    "instalmentAccounts",
    "attachments",
    "taxReport",
    "directDebitAmount",
    "directDebitAgreementDate",
    "directDebitBasetype",
    "legalPartClass",
    "minClaimFixedAmount",
    "budgetStatementAmount",
    "recommendedStatementAmount",
    "collection",
    "accountType",
    "accountCategory",
    "version",
    "statementType",
    "creditedInvoice"
})
@XmlRootElement(name = "Statement")
public class Statement {

    @XmlElement(name = "CustomerReference")
    protected int customerReference;
    @XmlElement(name = "MainSourceReference")
    protected int mainSourceReference;
    @XmlElement(name = "DistributionMethod", required = true)
    protected String distributionMethod;
    @XmlElement(name = "DistributionMethodExternalReference")
    protected long distributionMethodExternalReference;
    @XmlElement(name = "VatId", required = true)
    protected String vatId;
    @XmlElement(name = "PaymentType", required = true)
    protected String paymentType;
    @XmlElement(name = "PaymentTypeStatus", required = true)
    protected String paymentTypeStatus;
    @XmlElement(name = "EInvoiceId", required = true)
    protected String eInvoiceId;
    @XmlElement(name = "NationalId")
    protected int nationalId;
    @XmlElement(name = "Barcode", required = true)
    protected long barcode;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "CareOfName", required = true)
    protected String careOfName;
    @XmlElement(name = "Address", required = true)
    protected String address;
    @XmlElement(name = "Address1", required = true)
    protected String address1;
    @XmlElement(name = "Address2", required = true)
    protected String address2;
    @XmlElement(name = "PostCode")
    protected short postCode;
    @XmlElement(name = "City", required = true)
    protected String city;
    @XmlElement(name = "Region", required = true)
    protected String region;
    @XmlElement(name = "Country", required = true)
    protected String country;
    @XmlElement(name = "Email", required = true)
    protected String email;
    @XmlElement(name = "MobileNumber")
    protected int mobileNumber;
    @XmlElement(name = "StatementDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar statementDate;
    @XmlElement(name = "AccountNumber")
    protected long accountNumber;
    @XmlElement(name = "SequenceNumber")
    protected byte sequenceNumber;
    @XmlElement(name = "StatementOcrNumber")
    protected long statementOcrNumber;
    @XmlElement(name = "DueDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dueDate;
    @XmlElement(name = "IngoingBalance")
    protected float ingoingBalance;
    @XmlElement(name = "OutgoingBalance")
    protected float outgoingBalance;
    @XmlElement(name = "CreditLimit")
    protected float creditLimit;
    @XmlElement(name = "AvailableCredit")
    protected float availableCredit;
    @XmlElement(name = "NonCreditedInterest")
    protected float nonCreditedInterest;
    @XmlElement(name = "GeneralMessage", required = true)
    protected String generalMessage;
    @XmlElement(name = "AccountMessage", required = true)
    protected String accountMessage;
    @XmlElement(name = "CurrentClaim")
    protected float currentClaim;
    @XmlElement(name = "TotalOpenClaim")
    protected float totalOpenClaim;
    @XmlElement(name = "TotalVatAmount")
    protected float totalVatAmount;
    @XmlElement(name = "CurrentInterestRates", required = true)
    protected CurrentInterestRates currentInterestRates;
    @XmlElement(name = "OutstandingClaims", required = true)
    protected String outstandingClaims;
    @XmlElement(name = "Transactions", required = true)
    protected Transactions transactions;
    @XmlElement(name = "LineItems", required = true)
    protected String lineItems;
    @XmlElement(name = "InstalmentAccounts", required = true)
    protected String instalmentAccounts;
    @XmlElement(name = "Attachments", required = true)
    protected Attachments attachments;
    @XmlElement(name = "TaxReport", required = true)
    protected String taxReport;
    @XmlElement(name = "DirectDebitAmount", required = true)
    protected String directDebitAmount;
    @XmlElement(name = "DirectDebitAgreementDate", required = true)
    protected String directDebitAgreementDate;
    @XmlElement(name = "DirectDebitBasetype", required = true)
    protected String directDebitBasetype;
    @XmlElement(name = "LegalPartClass", required = true)
    protected String legalPartClass;
    @XmlElement(name = "MinClaimFixedAmount", required = true)
    protected String minClaimFixedAmount;
    @XmlElement(name = "BudgetStatementAmount")
    protected float budgetStatementAmount;
    @XmlElement(name = "RecommendedStatementAmount")
    protected float recommendedStatementAmount;
    @XmlElement(name = "Collection", required = true)
    protected String collection;
    @XmlElement(name = "AccountType", required = true)
    protected AccountType accountType;
    @XmlElement(name = "AccountCategory", required = true)
    protected AccountCategory accountCategory;
    @XmlElement(name = "Version")
    protected byte version;
    @XmlElement(name = "StatementType", required = true)
    protected String statementType;
    @XmlElement(name = "CreditedInvoice", required = true)
    protected String creditedInvoice;

    /**
     * Gets the value of the customerReference property.
     * 
     */
    public int getCustomerReference() {
        return customerReference;
    }

    /**
     * Sets the value of the customerReference property.
     * 
     */
    public void setCustomerReference(int value) {
        this.customerReference = value;
    }

    /**
     * Gets the value of the mainSourceReference property.
     * 
     */
    public int getMainSourceReference() {
        return mainSourceReference;
    }

    /**
     * Sets the value of the mainSourceReference property.
     * 
     */
    public void setMainSourceReference(int value) {
        this.mainSourceReference = value;
    }

    /**
     * Gets the value of the distributionMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistributionMethod() {
        return distributionMethod;
    }

    /**
     * Sets the value of the distributionMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistributionMethod(String value) {
        this.distributionMethod = value;
    }

    /**
     * Gets the value of the distributionMethodExternalReference property.
     * 
     */
    public long getDistributionMethodExternalReference() {
        return distributionMethodExternalReference;
    }

    /**
     * Sets the value of the distributionMethodExternalReference property.
     * 
     */
    public void setDistributionMethodExternalReference(long value) {
        this.distributionMethodExternalReference = value;
    }

    /**
     * Gets the value of the vatId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVatId() {
        return vatId;
    }

    /**
     * Sets the value of the vatId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVatId(String value) {
        this.vatId = value;
    }

    /**
     * Gets the value of the paymentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentType() {
        return paymentType;
    }

    /**
     * Sets the value of the paymentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentType(String value) {
        this.paymentType = value;
    }

    /**
     * Gets the value of the paymentTypeStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentTypeStatus() {
        return paymentTypeStatus;
    }

    /**
     * Sets the value of the paymentTypeStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentTypeStatus(String value) {
        this.paymentTypeStatus = value;
    }

    /**
     * Gets the value of the eInvoiceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEInvoiceId() {
        return eInvoiceId;
    }

    /**
     * Sets the value of the eInvoiceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEInvoiceId(String value) {
        this.eInvoiceId = value;
    }

    /**
     * Gets the value of the nationalId property.
     * 
     */
    public int getNationalId() {
        return nationalId;
    }

    /**
     * Sets the value of the nationalId property.
     * 
     */
    public void setNationalId(int value) {
        this.nationalId = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the careOfName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCareOfName() {
        return careOfName;
    }

    /**
     * Sets the value of the careOfName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCareOfName(String value) {
        this.careOfName = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
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
     * Gets the value of the postCode property.
     * 
     */
    public short getPostCode() {
        return postCode;
    }

    /**
     * Sets the value of the postCode property.
     * 
     */
    public void setPostCode(short value) {
        this.postCode = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the region property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the value of the region property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegion(String value) {
        this.region = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the mobileNumber property.
     * 
     */
    public int getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets the value of the mobileNumber property.
     * 
     */
    public void setMobileNumber(int value) {
        this.mobileNumber = value;
    }

    /**
     * Gets the value of the statementDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStatementDate() {
        return statementDate;
    }

    /**
     * Sets the value of the statementDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setStatementDate(XMLGregorianCalendar value) {
        this.statementDate = value;
    }

    /**
     * Gets the value of the accountNumber property.
     * 
     */
    public long getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     */
    public void setAccountNumber(long value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the sequenceNumber property.
     * 
     */
    public byte getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the value of the sequenceNumber property.
     * 
     */
    public void setSequenceNumber(byte value) {
        this.sequenceNumber = value;
    }

    /**
     * Gets the value of the statementOcrNumber property.
     * 
     */
    public long getStatementOcrNumber() {
        return statementOcrNumber;
    }

    /**
     * Sets the value of the statementOcrNumber property.
     * 
     */
    public void setStatementOcrNumber(long value) {
        this.statementOcrNumber = value;
    }

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
     * Gets the value of the ingoingBalance property.
     * 
     */
    public float getIngoingBalance() {
        return ingoingBalance;
    }

    /**
     * Sets the value of the ingoingBalance property.
     * 
     */
    public void setIngoingBalance(float value) {
        this.ingoingBalance = value;
    }

    /**
     * Gets the value of the outgoingBalance property.
     * 
     */
    public float getOutgoingBalance() {
        return outgoingBalance;
    }

    /**
     * Sets the value of the outgoingBalance property.
     * 
     */
    public void setOutgoingBalance(float value) {
        this.outgoingBalance = value;
    }

    /**
     * Gets the value of the creditLimit property.
     * 
     */
    public float getCreditLimit() {
        return creditLimit;
    }

    /**
     * Sets the value of the creditLimit property.
     * 
     */
    public void setCreditLimit(float value) {
        this.creditLimit = value;
    }

    /**
     * Gets the value of the availableCredit property.
     * 
     */
    public float getAvailableCredit() {
        return availableCredit;
    }

    /**
     * Sets the value of the availableCredit property.
     * 
     */
    public void setAvailableCredit(float value) {
        this.availableCredit = value;
    }

    /**
     * Gets the value of the nonCreditedInterest property.
     * 
     */
    public float getNonCreditedInterest() {
        return nonCreditedInterest;
    }

    /**
     * Sets the value of the nonCreditedInterest property.
     * 
     */
    public void setNonCreditedInterest(float value) {
        this.nonCreditedInterest = value;
    }

    /**
     * Gets the value of the generalMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeneralMessage() {
        return generalMessage;
    }

    /**
     * Sets the value of the generalMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeneralMessage(String value) {
        this.generalMessage = value;
    }

    /**
     * Gets the value of the accountMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountMessage() {
        return accountMessage;
    }

    /**
     * Sets the value of the accountMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountMessage(String value) {
        this.accountMessage = value;
    }

    /**
     * Gets the value of the currentClaim property.
     * 
     */
    public float getCurrentClaim() {
        return currentClaim;
    }

    /**
     * Sets the value of the currentClaim property.
     * 
     */
    public void setCurrentClaim(float value) {
        this.currentClaim = value;
    }

    /**
     * Gets the value of the totalOpenClaim property.
     * 
     */
    public float getTotalOpenClaim() {
        return totalOpenClaim;
    }

    /**
     * Sets the value of the totalOpenClaim property.
     * 
     */
    public void setTotalOpenClaim(float value) {
        this.totalOpenClaim = value;
    }

    /**
     * Gets the value of the totalVatAmount property.
     * 
     */
    public float getTotalVatAmount() {
        return totalVatAmount;
    }

    /**
     * Sets the value of the totalVatAmount property.
     * 
     */
    public void setTotalVatAmount(float value) {
        this.totalVatAmount = value;
    }

    /**
     * Gets the value of the currentInterestRates property.
     * 
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.CurrentInterestRates }
     *     
     */
    public CurrentInterestRates getCurrentInterestRates() {
        return currentInterestRates;
    }

    /**
     * Sets the value of the currentInterestRates property.
     * 
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.CurrentInterestRates }
     *     
     */
    public void setCurrentInterestRates(CurrentInterestRates value) {
        this.currentInterestRates = value;
    }

    /**
     * Gets the value of the outstandingClaims property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutstandingClaims() {
        return outstandingClaims;
    }

    /**
     * Sets the value of the outstandingClaims property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutstandingClaims(String value) {
        this.outstandingClaims = value;
    }

    /**
     * Gets the value of the transactions property.
     * 
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.Transactions }
     *     
     */
    public Transactions getTransactions() {
        return transactions;
    }

    /**
     * Sets the value of the transactions property.
     * 
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.Transactions }
     *     
     */
    public void setTransactions(Transactions value) {
        this.transactions = value;
    }

    /**
     * Gets the value of the lineItems property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLineItems() {
        return lineItems;
    }

    /**
     * Sets the value of the lineItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLineItems(String value) {
        this.lineItems = value;
    }

    /**
     * Gets the value of the instalmentAccounts property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstalmentAccounts() {
        return instalmentAccounts;
    }

    /**
     * Sets the value of the instalmentAccounts property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstalmentAccounts(String value) {
        this.instalmentAccounts = value;
    }

    /**
     * Gets the value of the attachments property.
     * 
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.Attachments }
     *     
     */
    public Attachments getAttachments() {
        return attachments;
    }

    /**
     * Sets the value of the attachments property.
     * 
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.Attachments }
     *     
     */
    public void setAttachments(Attachments value) {
        this.attachments = value;
    }

    /**
     * Gets the value of the taxReport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxReport() {
        return taxReport;
    }

    /**
     * Sets the value of the taxReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxReport(String value) {
        this.taxReport = value;
    }

    /**
     * Gets the value of the directDebitAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirectDebitAmount() {
        return directDebitAmount;
    }

    /**
     * Sets the value of the directDebitAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirectDebitAmount(String value) {
        this.directDebitAmount = value;
    }

    /**
     * Gets the value of the directDebitAgreementDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirectDebitAgreementDate() {
        return directDebitAgreementDate;
    }

    /**
     * Sets the value of the directDebitAgreementDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirectDebitAgreementDate(String value) {
        this.directDebitAgreementDate = value;
    }

    /**
     * Gets the value of the directDebitBasetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirectDebitBasetype() {
        return directDebitBasetype;
    }

    /**
     * Sets the value of the directDebitBasetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirectDebitBasetype(String value) {
        this.directDebitBasetype = value;
    }

    /**
     * Gets the value of the legalPartClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegalPartClass() {
        return legalPartClass;
    }

    /**
     * Sets the value of the legalPartClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegalPartClass(String value) {
        this.legalPartClass = value;
    }

    /**
     * Gets the value of the minClaimFixedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinClaimFixedAmount() {
        return minClaimFixedAmount;
    }

    /**
     * Sets the value of the minClaimFixedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinClaimFixedAmount(String value) {
        this.minClaimFixedAmount = value;
    }

    /**
     * Gets the value of the budgetStatementAmount property.
     * 
     */
    public float getBudgetStatementAmount() {
        return budgetStatementAmount;
    }

    /**
     * Sets the value of the budgetStatementAmount property.
     * 
     */
    public void setBudgetStatementAmount(float value) {
        this.budgetStatementAmount = value;
    }

    /**
     * Gets the value of the recommendedStatementAmount property.
     * 
     */
    public float getRecommendedStatementAmount() {
        return recommendedStatementAmount;
    }

    /**
     * Sets the value of the recommendedStatementAmount property.
     * 
     */
    public void setRecommendedStatementAmount(float value) {
        this.recommendedStatementAmount = value;
    }

    /**
     * Gets the value of the collection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollection() {
        return collection;
    }

    /**
     * Sets the value of the collection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollection(String value) {
        this.collection = value;
    }

    /**
     * Gets the value of the accountType property.
     * 
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.AccountType }
     *     
     */
    public AccountType getAccountType() {
        return accountType;
    }

    /**
     * Sets the value of the accountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.AccountType }
     *     
     */
    public void setAccountType(AccountType value) {
        this.accountType = value;
    }

    /**
     * Gets the value of the accountCategory property.
     * 
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.AccountCategory }
     *     
     */
    public AccountCategory getAccountCategory() {
        return accountCategory;
    }

    /**
     * Sets the value of the accountCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.AccountCategory }
     *     
     */
    public void setAccountCategory(AccountCategory value) {
        this.accountCategory = value;
    }

    /**
     * Gets the value of the version property.
     * 
     */
    public byte getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     */
    public void setVersion(byte value) {
        this.version = value;
    }

    /**
     * Gets the value of the statementType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatementType() {
        return statementType;
    }

    /**
     * Sets the value of the statementType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatementType(String value) {
        this.statementType = value;
    }

    /**
     * Gets the value of the creditedInvoice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreditedInvoice() {
        return creditedInvoice;
    }

    /**
     * Sets the value of the creditedInvoice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreditedInvoice(String value) {
        this.creditedInvoice = value;
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }
}
