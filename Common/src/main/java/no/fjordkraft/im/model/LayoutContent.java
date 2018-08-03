package no.fjordkraft.im.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by miles on 6/30/2017.
 */
@Entity
@Table(name="IM_LAYOUT_CONTENT")
public class LayoutContent {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_LAYOUT_CONTENT_SEQ")
    private Long id;

    @Column(name="LAYOUT_ID")
    private Long layoutId;

    @Column(name="FILE_CONTENT")
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String fileContent;

    @Column(name="VERSION")
    private Integer version;

    @Column(name="ACTIVE")
    private boolean active;

    @Column(name="CREATED_TMS")
    private Timestamp createdTms;

    @Column(name="UPDATE_TMS")
    private Timestamp updateTms;

    @Transient
    private String reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedTms() {
        return createdTms;
    }

    public void setCreatedTms(Timestamp createdTms) {
        this.createdTms = createdTms;
    }

    public Timestamp getUpdateTms() {
        return updateTms;
    }

    public void setUpdateTms(Timestamp updateTms) {
        this.updateTms = updateTms;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
