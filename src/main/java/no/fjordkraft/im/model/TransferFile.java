package no.fjordkraft.im.model;

import javax.persistence.*;
import java.sql.Clob;
import java.sql.Timestamp;

/**
 * Created by miles on 5/4/2017.
 */
@Table(name="TRANSFERFILE")
@Entity
public class TransferFile {
    @Column(name="ID")
    @Id
    private Long id;
    @Column(name="TRANSFERTYPE")
    private String transferType;
    @Column(name="BRAND")
    private String brand;
    @Column(name="FILENAME")
    private String fileName;
    @Column(name="CREATED")
    private Timestamp created;
    @Column(name="EKBATCHJOBID")
    private Long ekBatchJobId;
    @Column(name="TRANSFERSTATUS")
    private String transferState;
    @Column(name="STATUSUPDATED")
    private Timestamp statusUpdated;
    @Column(name="FILESIZE")
    private Long fileSize;
    @Column(name="TTRANSFERREDBYTES")
    private Long transferredBytes;
    @Column(name="FILESTORED")
    private Timestamp fileStored;
    @Column(name="FILECONTENT")
    @Lob
    private String fileContent;
    @Column(name="UPLOADSTARTED")
    private Timestamp uploadStarted;
    @Column(name="UPLOADENDED")
    private Timestamp uploadEnded;
    @Column(name="EHF")
    private Integer ehf;
    @Column(name="COMPELLOUPLOADSTARTED")
    private Timestamp compelloUploadStarted;
    @Column(name="COMPELLOULOADENDED")
    private Timestamp compelloUploadEnded;

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Long getEkBatchJobId() {
        return ekBatchJobId;
    }

    public void setEkBatchJobId(Long ekBatchJobId) {
        this.ekBatchJobId = ekBatchJobId;
    }

    public String getTransferState() {
        return transferState;
    }

    public void setTransferState(String transferState) {
        this.transferState = transferState;
    }

    public Timestamp getStatusUpdated() {
        return statusUpdated;
    }

    public void setStatusUpdated(Timestamp statusUpdated) {
        this.statusUpdated = statusUpdated;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getTransferredBytes() {
        return transferredBytes;
    }

    public void setTransferredBytes(Long transferredBytes) {
        this.transferredBytes = transferredBytes;
    }

    public Timestamp getFileStored() {
        return fileStored;
    }

    public void setFileStored(Timestamp fileStored) {
        this.fileStored = fileStored;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public Timestamp getUploadStarted() {
        return uploadStarted;
    }

    public void setUploadStarted(Timestamp uploadStarted) {
        this.uploadStarted = uploadStarted;
    }

    public Timestamp getUploadEnded() {
        return uploadEnded;
    }

    public void setUploadEnded(Timestamp uploadEnded) {
        this.uploadEnded = uploadEnded;
    }

    public Integer getEhf() {
        return ehf;
    }

    public void setEhf(Integer ehf) {
        this.ehf = ehf;
    }

    public Timestamp getCompelloUploadStarted() {
        return compelloUploadStarted;
    }

    public void setCompelloUploadStarted(Timestamp compelloUploadStarted) {
        this.compelloUploadStarted = compelloUploadStarted;
    }

    public Timestamp getCompelloUploadEnded() {
        return compelloUploadEnded;
    }

    public void setCompelloUploadEnded(Timestamp compelloUploadEnded) {
        this.compelloUploadEnded = compelloUploadEnded;
    }
}
