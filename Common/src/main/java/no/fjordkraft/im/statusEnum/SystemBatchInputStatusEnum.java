package no.fjordkraft.im.statusEnum;

/**
 * Created by miles on 5/8/2017.
 */
public enum SystemBatchInputStatusEnum {
    PENDING("PENDING"),
    PROCESSING("PROCESSING"),
    PROCESSED("PROCESSED"),
    FAILED("FAILED"),
    PARTIAL_PROCESSED("PARTIAL_PROCESSED"),
    ALL_PROCESSED("ALL_PROCESSED");

    private String status;
    SystemBatchInputStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static SystemBatchInputStatusEnum fromString(String text) {
        for (SystemBatchInputStatusEnum systemBatchInputStatusEnum : SystemBatchInputStatusEnum.values()) {
            if (systemBatchInputStatusEnum.status.equalsIgnoreCase(text)) {
                return systemBatchInputStatusEnum;
            }
        }
        return null;
    }
}
