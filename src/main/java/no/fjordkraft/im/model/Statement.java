package no.fjordkraft.im.model;

import javax.persistence.*;
import java.sql.Timestamp;

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

    /*@Column(name="SI_ID")
    private Long siId;*/

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name="SI_ID")
    private  SystemBatchInput systemBatchInput;

    @Column(name="PAYLOAD", updatable=false)
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String payload;

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

    @Column(name="Update_TIME")
    private Timestamp udateTime;

    @Column(name="PDF_ATTACHMENT")
    private Integer pdfAttachment;

    @Column(name="CITY")
    private String city;

    @Column(name="VERSION")
    private Byte version;

    @Column(name="DISTRIBUTION_METHOD")
    private String distributionMethod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
    }

    /*public Long getSiId() {
        return siId;
    }

    public void setSiId(Long siId) {
        this.siId = siId;
    }*/

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
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
}
