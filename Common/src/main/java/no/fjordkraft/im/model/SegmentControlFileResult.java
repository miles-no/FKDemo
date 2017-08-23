package no.fjordkraft.im.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

/**
 * Created by miles on 6/21/2017.
 */
//@Table(name="SEGMENTCONTROLFILERESULT", schema="eacprod")
@Table(name="SEGMENTCONTROLFILERESULT")
@Entity
public class SegmentControlFileResult {

    @Column(name="ID")
    @Id
    private Long id;

    @Column(name="FROMDATE")
    private Date fromDate;

    @Column(name="BRAND")
    private String brand;

    @Column(name="CUSTOMERTYPE")
    private String customerType;

    @Column(name="ACCOUNTNO")
    private String accountNo;

    @Column(name="ATTACH_FILENAME")
    private String attachFilename;

    @Column(name="CAMPAIGN_FILENAME")
    private String campaignFilename;

    @Column(name="ID_ATTACH")
    private Integer idAttach;

    @Column(name="ID_CAMPAIGN")
    private Integer idCampaign;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAttachFilename() {
        return attachFilename;
    }

    public void setAttachFilename(String attachFilename) {
        this.attachFilename = attachFilename;
    }

    public String getCampaignFilename() {
        return campaignFilename;
    }

    public void setCampaignFilename(String campaignFilename) {
        this.campaignFilename = campaignFilename;
    }

    public Integer getIdCampaign() {
        return idCampaign;
    }

    public void setIdCampaign(Integer idCampaign) {
        this.idCampaign = idCampaign;
    }

    public Integer getIdAttach() {
        return idAttach;
    }

    public void setIdAttach(Integer idAttach) {
        this.idAttach = idAttach;
    }
}
