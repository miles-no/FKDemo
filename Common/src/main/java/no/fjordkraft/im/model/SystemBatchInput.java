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
    private Long id;

    @Column(name="STATUS")
    private String status;
    @Column(name="CREATE_TIME")
    private Timestamp createTime;
    @Column(name="UPDATE_TIME")
    private Timestamp updateTime;
    @Column(name="NUM_OF_RECORDS")
    private Integer numOfRecords;

    @JoinColumns({
            @JoinColumn(name="transfertype", referencedColumnName="transferType"),
            @JoinColumn(name="brand", referencedColumnName="brand"),
            @JoinColumn(name="filename", referencedColumnName="filename"),
    })
    @ManyToOne(fetch = FetchType.LAZY)
    public TransferFile transferFile;

    @Column(name="brand", insertable= false, updatable = false)
    private String brand;

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

    public TransferFile getTransferFile() {
        return transferFile;
    }

    public void setTransferFile(TransferFile transferFile) {
        this.transferFile = transferFile;
    }

    public Integer getNumOfRecords() {
        return numOfRecords;
    }

    public void setNumOfRecords(Integer numOfRecords) {
        this.numOfRecords = numOfRecords;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
