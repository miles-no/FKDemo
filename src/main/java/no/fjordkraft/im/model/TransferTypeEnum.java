package no.fjordkraft.im.model;

/**
 * Created by bhavi on 6/27/2017.
 */
public enum TransferTypeEnum {

    if320(".*\\.xml"),
    directdebit(".*\\.txt");

    private String fileFilter;

    TransferTypeEnum(String fileFilter) {
        this.fileFilter = fileFilter;
    }

    public String getFileFilter() {
        return this.fileFilter;
    }
}
