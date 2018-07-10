package no.fjordkraft.im.statusEnum;

/**
 * Created by miles on 5/2/2017.
 */
public enum StatementStatusEnum {
    PENDING("PENDING"),
    PRE_PROCESSING("PRE_PROCESSING"),
    PRE_PROCESSING_FAILED("PRE_PROCESSING_FAILED"),
    PRE_PROCESSED("PRE_PROCESSED"),
    SENT_FOR_PDF_PROCESSING("SENT_FOR_PDF_PROCESSING"),
    PDF_PROCESSING("PDF_PROCESSING"),
    PDF_PROCESSING_FAILED("PDF_PROCESSING_FAILED"),
    PDF_PROCESSED("PDF_PROCESSED"),
    INVOICE_PROCESSING("INVOICE_PROCESSING"),
    INVOICE_PROCESSING_FAILED("INVOICE_PROCESSING_FAILED"),
    INVOICE_PROCESSED("INVOICE_PROCESSED"),
    DELIVERY_PENDING("DELIVERY_PENDING"),
    DELIVERED("DELIVERED"),
    DELIVERY_FAILED("DELIVERY_FAILED"),
    NEW("NEW");

    private String status;

    StatementStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    private StatementStatusEnum() { }

    public static StatementStatusEnum fromString(final String s) {
        return StatementStatusEnum.valueOf(s);
    }
}
