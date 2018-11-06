package no.fjordkraft.im.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 11/2/18
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Table(name="IM_ACCOUNT_ATTACHMENT_MAPPING")
@Entity
public class AccountAttachmentMapping {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_ACCOUNT_ATCHMNT_MAP_SEQ")
    private Long id;

    @Column(name="ACCOUNT_NO")
    private String accountNo;

    @Column(name="CUSTOMER_ID")
    private String customerID;

    @Column(name="IS_ACTIVE")
    private boolean isActive;

    @Column(name="CREATE_TIME")
    private Timestamp createTime;

    @Column(name="UPDATE_TIME")
    private Timestamp updateTime;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE}, fetch = FetchType.EAGER )
    @JoinColumn(name="ACCOUNT_ATTACHMENT_ID")
    private AccountAttachment accountAttachment;

    @Column(name="CREATED_BY")
    private String createdBy;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public AccountAttachment getAccountAttachment() {
        return accountAttachment;
    }

    public void setAccountAttachment(AccountAttachment accountAttachment) {
        this.accountAttachment = accountAttachment;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
