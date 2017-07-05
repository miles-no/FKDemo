package no.fjordkraft.im.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by bhavi on 6/27/2017.
 */
@Entity
@IdClass(TransferFileId.class)
@Table(name = "transferfile",schema = "eacprod")
public class TransferFileArchive {

    @Id
    @Column(name = "transfertype")
    @Enumerated(EnumType.STRING)
    TransferTypeEnum transferType;

    @Id
    @Column(name = "brand")
    String brand;

    @Id
    @Column(name = "filename")
    String filename;

    @Column(name = "filesize")
    Long fileSize;

    @Column(name = "filestored")
    private Timestamp fileStored;

    @Column(name = "filecontent")
    String fileContent;

    public TransferFileArchive() {
    }

    public TransferFileArchive(TransferTypeEnum transferType, String brand, String filename) {
        setTransferType(transferType);
        setBrand(brand);
        setFilename(filename);
    }

    public TransferFileId getCompositeKey() {
        TransferFileId tfId = new TransferFileId();
        tfId.setTransferType(getTransferType());
        tfId.setBrand(getBrand());
        tfId.setFilename(getFilename());
        return tfId;
    }

    public String getFileContent() {
        return fileContent;
    }

    public Timestamp getFileStored() {
        return fileStored;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getFilename() {
        return filename;
    }

    public String getBrand() {
        return brand;
    }

    public TransferTypeEnum getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferTypeEnum transferType) {
        this.transferType = transferType;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setFileStored(Timestamp fileStored) {
        this.fileStored = fileStored;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }
}
