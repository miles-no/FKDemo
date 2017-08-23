package no.fjordkraft.im.domain;

import no.fjordkraft.im.model.InvoicePdf;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bhavi on 6/8/2017.
 */
public class RestStatement {


    private Long id;

    private String status;

    private String statementId;

    private String statementType;

    private String invoiceNumber;

    private String accountNumber;

    private String customerId;

    private Timestamp createTime;

    private Timestamp udateTime;

    private Integer pdfAttachment;

    private String city;

    private Byte version;

    private String distributionMethod;

    private float amount;

    private Date invoiceDate;

    private Date dueDate;

    private String brand;

    private List<RestInvoicePdf> invoicePdfList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUdateTime() {
        return udateTime;
    }

    public void setUdateTime(Timestamp udateTime) {
        this.udateTime = udateTime;
    }

    public Integer getPdfAttachment() {
        return pdfAttachment;
    }

    public void setPdfAttachment(Integer pdfAttachment) {
        this.pdfAttachment = pdfAttachment;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }

    public String getDistributionMethod() {
        return distributionMethod;
    }

    public void setDistributionMethod(String distributionMethod) {
        this.distributionMethod = distributionMethod;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public List<RestInvoicePdf> getInvoicePdfList() {
        if(null == invoicePdfList) {
            invoicePdfList = new ArrayList<RestInvoicePdf>();
        }
        return invoicePdfList;
    }

    public void setInvoicePdfList(List<InvoicePdf> RestInvoicePdf) {
        this.invoicePdfList = invoicePdfList;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
