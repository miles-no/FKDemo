package no.fjordkraft.im.model;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/10/18
 * Time: 10:39 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name= "IM_ATTACHMENT" )
public class Attachment {

    @Id
    @Column(name="ATTACHMENT_ID")
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_ATTACHMENT_SEQ")
    private long attachmentID;

    @Column(name="ATTACHMENT_TYPE")
    private String attachmentType;

    @Column(name="ATTACHMENT_CONTENT")
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String fileContent;

    @Column(name="BRAND")
    private String brand;

    @Column(name="FILE_TYPE")
    private String fileType;

    @OneToOne
    @JoinColumn(name="ATTACHMENT_CONFIG_ID")
    private AttachmentConfig attachmentConfig;


    public long getAttachmentID() {
        return attachmentID;
    }

    public void setAttachmentID(long attachmentID) {
        this.attachmentID = attachmentID;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public AttachmentConfig getAttachmentConfig() {
        return attachmentConfig;
    }

    public void setAttachmentConfig(AttachmentConfig attachmentConfig) {
        this.attachmentConfig = attachmentConfig;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
