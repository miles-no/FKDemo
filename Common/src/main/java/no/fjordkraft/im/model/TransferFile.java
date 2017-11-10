package no.fjordkraft.im.model;

/**
 * Created by bhavi on 6/27/2017.
 */
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@IdClass(TransferFileId.class)
@Table(name = "transferfile",schema="eacprod")
public class TransferFile implements Serializable{

    @Id
    @Column(name = "transfertype",updatable = false)
    @Enumerated(EnumType.STRING)
    TransferTypeEnum transferType;

    @Id
    @Column(name = "brand",updatable = false)
    String brand;

    @Id
    @Column(name = "filename",updatable = false)
    String filename;

    @Column(name = "created",updatable = false)
    private Timestamp created;

    @Column(name = "ekbatchjobid",updatable = false)
    private Long ekBatchJobId;

    @Column(name = "ehf",updatable = false)
    private Boolean ehf;

    /*@Column(name = "transferstatus",updatable = false)
    @Enumerated(EnumType.STRING)
    TransferStatusEnum transferStatus;*/

    @Column(name = "statusupdated",updatable = false)
    Timestamp statusUpdated;

    @Column(name = "filesize",updatable = false)
    Long fileSize;

    @Column(name = "transferredbytes",updatable = false)
    Long transferredBytes;

    @Column(name = "filestored",updatable = false)
    private Timestamp fileStored;

    @Column(name = "uploadstarted",updatable = false)
    private Timestamp uploadStarted;

    @Column(name = "uploadended",updatable = false)
    private Timestamp uploadEnded;

    @Column(name = "compellouploadstarted",updatable = false)
    private Timestamp compelloUploadStarted;

    @Column(name = "compellouploadended",updatable = false)
    private Timestamp compelloUploadEnded;

    @Column(name = "IMSTATUS")
    private String imStatus;

    @Column(name = "IMSTATUSCHANGED")
    private Timestamp imSatusChanged;

    @Column(name = "INVOICEMANAGER",updatable = false)
    private Boolean invoiceManager;

    public TransferFile() {
    }

    public TransferFile(TransferTypeEnum transferType, String brand, String filename) {
        setTransferType(transferType);
        setBrand(brand);
        setFilename(filename);
    }

    public TransferFile(String filename, Long fileSize) {
        setFilename(filename);
        setFileSize(fileSize);
    }


    public TransferFileId getCompositeKey() {
        TransferFileId tfId = new TransferFileId();
        tfId.setTransferType(getTransferType());
        tfId.setBrand(getBrand());
        tfId.setFilename(getFilename());
        return tfId;
    }


    public Timestamp getCompelloUploadEnded() {
        return compelloUploadEnded;
    }

    public void setCompelloUploadEnded(Timestamp compelloUploadEnded) {
        this.compelloUploadEnded = compelloUploadEnded;
    }

    public Timestamp getCompelloUploadStarted() {
        return compelloUploadStarted;
    }

    public void setCompelloUploadStarted(Timestamp compelloUploadStarted) {
        this.compelloUploadStarted = compelloUploadStarted;
    }

    public Timestamp getUploadEnded() {
        return uploadEnded;
    }

    public void setUploadEnded(Timestamp uploadEnded) {
        this.uploadEnded = uploadEnded;
    }

    public Timestamp getUploadStarted() {
        return uploadStarted;
    }

    public void setUploadStarted(Timestamp uploadStarted) {
        this.uploadStarted = uploadStarted;
    }

    public Timestamp getFileStored() {
        return fileStored;
    }

    public void setFileStored(Timestamp fileStored) {
        this.fileStored = fileStored;
    }

    public Long getTransferredBytes() {
        return transferredBytes;
    }

    public void setTransferredBytes(Long transferredBytes) {
        this.transferredBytes = transferredBytes;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Timestamp getStatusUpdated() {
        return statusUpdated;
    }

    public void setStatusUpdated(Timestamp statusUpdated) {
        this.statusUpdated = statusUpdated;
    }

    /*public TransferStatusEnum getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatusEnum transferStatus) {
        this.transferStatus = transferStatus;
    }*/

    public Boolean getEhf() {
        return ehf;
    }

    public void setEhf(Boolean ehf) {
        this.ehf = ehf;
    }

    public Long getEkBatchJobId() {
        return ekBatchJobId;
    }

    public void setEkBatchJobId(Long ekBatchJobId) {
        this.ekBatchJobId = ekBatchJobId;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public TransferTypeEnum getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferTypeEnum transferType) {
        this.transferType = transferType;
    }

    public String getImStatus() {
        return imStatus;
    }

    public void setImStatus(String imStatus) {
        this.imStatus = imStatus;
    }

    public Timestamp getImSatusChanged() {
        return imSatusChanged;
    }

    public void setImSatusChanged(Timestamp imSatusChanged) {
        this.imSatusChanged = imSatusChanged;
    }

    public Boolean getInvoiceManager() {
        return invoiceManager;
    }

    public void setInvoiceManager(Boolean invoiceManager) {
        this.invoiceManager = invoiceManager;
    }
}

