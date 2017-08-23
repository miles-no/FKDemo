package no.fjordkraft.im.statusEnum;

/**
 * Created by miles on 5/26/2017.
 */
public enum UIStatementStatusEnum {

    PENDING("PENDING"),
    PRE_PROCESSING("PRE-PROCESSING"),
    PROCESSING("PROCESSING"),
    MERGING("MERGING"),
    READY("READY"),
    FAILED("FAILED");

    private String status;

    UIStatementStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    private UIStatementStatusEnum() { }

    public static UIStatementStatusEnum fromString(final String s) {
        return UIStatementStatusEnum.valueOf(s);
    }
}
