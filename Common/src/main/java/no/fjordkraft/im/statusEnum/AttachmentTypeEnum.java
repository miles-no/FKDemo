package no.fjordkraft.im.statusEnum;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/16/18
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public enum AttachmentTypeEnum {

    FIRST_TIME_ATTACHMENT(1),
    FULL_KONTROLL_ATTACHMENT(2),
    OTHER_ATTACHMENT(3);

    private int status;

    AttachmentTypeEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    private AttachmentTypeEnum() { }

    public static AttachmentTypeEnum fromString(final String s) {
        return AttachmentTypeEnum.valueOf(s);
    }
}
