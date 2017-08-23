package no.fjordkraft.im.model;

import javax.persistence.*;

/**
 * Created by miles on 5/8/2017.
 */
@Table(name="IM_EXPORT")
@Entity
public class Export {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_EXPORT_SEQ")
    private Long id;

    @Column(name="INVOICE_NUMBER")
    String invoiceNumber;
    @Column(name="CUSTOMER_ID")
    String customerId;
    @Column(name="ACCOUNT_NUMBER")
    String accountNumber;
    @Column(name="ATTACHMENT")
    @Lob
    String attachment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
