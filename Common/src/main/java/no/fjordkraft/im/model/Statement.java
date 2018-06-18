package no.fjordkraft.im.model;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by miles on 5/2/2017.
 */
@Table(name="IM_STATEMENT")
@Entity
public class Statement {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_STATEMENT_SEQ")
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name="SI_ID")
    private SystemBatchInput systemBatchInput;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE}, mappedBy = "statement", fetch = FetchType.LAZY)
    private StatementPayload statementPayload;

    @Column(name="STATUS")
    private String status;

    @Column(name="STATEMENT_ID")
    private String statementId;

    @Column(name="STATEMENT_TYPE")
    private String statementType;

    @Column(name="INVOICE_NUMBER")
    private String invoiceNumber;

    @Column(name="ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name="CUSTOMER_ID")
    private String customerId;

    @Column(name="CREATE_TIME")
    private Timestamp createTime;

    //@Version
    @Column(name="Update_TIME")
    private Timestamp updateTime;

    @Column(name="PDF_ATTACHMENT")
    private Integer pdfAttachment;

    @Column(name="CITY")
    private String city;

    @Column(name="VERSION")
    private Byte version;

    @Column(name="DISTRIBUTION_METHOD")
    private String distributionMethod;

    @Column(name="AMOUNT")
    private float amount;

    @Column(name="INVOICE_DATE")
    private Date invoiceDate;

    @Column(name="DUE_DATE")
    private Date dueDate;

    @Column(name="LAYOUT_ID")
    private Long layoutID;

    @Transient
    private String brand;

    @Column(name="CREDIT_LIMIT")
    private float creditLimit;

    @Column(name="ATTACHMENT_CONFIG_ID")
    private int attachmentConfigId;

    @Column(name="LEGAL_PART_CLASS")
    private String legalPartClass;

    @Transient
    private boolean isOnline = false;

    @Transient
    private byte[] generatedPDF;

    @Transient
    private String fileName;

    @Transient
    private String seqNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
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

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getPdfAttachment() {
        return pdfAttachment;
    }

    public void setPdfAttachment(Integer pdfAttachment) {
        this.pdfAttachment = pdfAttachment;
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

    public SystemBatchInput getSystemBatchInput() {
        return systemBatchInput;
    }

    public void setSystemBatchInput(SystemBatchInput systemBatchInput) {
        this.systemBatchInput = systemBatchInput;
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

    public StatementPayload getStatementPayload() {
        return statementPayload;
    }

    public void setStatementPayload(StatementPayload statementPayload) {
        this.statementPayload = statementPayload;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getLayoutID() {
        return layoutID;
    }

    public void setLayoutID(Long layoutID) {
        this.layoutID = layoutID;
    }

    public float getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(float creditLimit) {
        this.creditLimit = creditLimit;
    }

    public int getAttachmentConfigId() {
        return attachmentConfigId;
    }

    public void setAttachmentConfigId(int attachmentConfigId) {
        this.attachmentConfigId = attachmentConfigId;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public byte[] getGeneratedPDF() {
        return generatedPDF;
    }

    public void setGeneratedPDF(byte[] generatedPDF) {
        this.generatedPDF = generatedPDF;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getLegalPartClass() {
        return legalPartClass;
    }

    public void setLegalPartClass(String legalPartClass) {
        this.legalPartClass = legalPartClass;
    }
}
