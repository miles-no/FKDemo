package no.fjordkraft.im.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by miles on 5/5/2017.
 */
@Table(name="IM_SYSTEM_BATCH_INPUT")
@Entity
public class SystemBatchInput {
    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_SYSTEM_BATCH_INPUT_SEQ")
    private Long Id;

    @Column(name="TF_ID")
    private Long tfId;
    @Column(name="FILENAME")
    private String filename;

    @Basic(fetch = FetchType.LAZY)
    @Column(name="PAYLOAD")
    @Lob
    private String payload;
    @Column(name="STATUS")
    private String status;
    @Column(name="CREATE_TIME")
    private Timestamp createTime;
    @Column(name="UPDATE_TIME")
    private Timestamp updateTime;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getTfId() {
        return tfId;
    }

    public void setTfId(Long tfId) {
        this.tfId = tfId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

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
