package no.fjordkraft.im.model;


import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/10/18
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="IM_ATTACHMENT_CONFIG" )
public class AttachmentConfig {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_ATTACHMENT_CONFIG_SEQ")
    private long id;

    @Column(name="ATTACHMENT_NAME")
    private String attachmentName;

    @Column(name="CREATE_TIME")
    private Timestamp createTime;

    @Column(name="UPDATE_TIME")
    private Timestamp updateTime;

  /*  @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name="ATTACHMENT_ID")
    private Attachment attachment;

    @Column(name="BRAND")
    private String brand;*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
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
}
