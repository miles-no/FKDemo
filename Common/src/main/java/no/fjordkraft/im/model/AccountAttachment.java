package no.fjordkraft.im.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 10/26/18
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "IM_ACCOUNT_ATTACHMENT")
@Entity
public class AccountAttachment {

    @Column(name="ACCOUNT_ATTACHMENT_ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_ACCOUNT_ATTACHMENT_SEQ")
    private Long id;

    @Column(name="BRAND")
    private String brand;

    @Column(name="ATTACHMENT_TYPE")
    private String attachmentType;

    @Column(name="FILE_CONTENT")
    @Lob
    private String fileContent;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name="ATTACHMENT_CONFIG_TYP")
    private String attachmentConfigType;

    @Column(name="CREATE_TIME")
    private Timestamp createTime;

    @Column(name="UPDATE_TIME")
    private Timestamp updateTime;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="CREATED_BY")
    private String createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachmentConfigType() {
        return attachmentConfigType;
    }

    public void setAttachmentConfigType(String attachmentConfigType) {
        this.attachmentConfigType = attachmentConfigType;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
