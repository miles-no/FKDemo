package no.fjordkraft.im.model;


import java.io.Serializable;


public class TransferFileId implements Serializable {
    TransferTypeEnum transferType;
    String brand;
    String filename;

    public TransferFileId() {
    }

    public static TransferFileId one(TransferTypeEnum transferType,
                                     String brand,
                                     String filename) {
        TransferFileId r = new TransferFileId();
        r.setTransferType(transferType);
        r.setBrand(brand);
        r.setFilename(filename);
        return r;
    }

    public TransferTypeEnum getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferTypeEnum transferType) {
        this.transferType = transferType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}