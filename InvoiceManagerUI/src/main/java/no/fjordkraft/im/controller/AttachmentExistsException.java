package no.fjordkraft.im.controller;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 2/5/18
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class AttachmentExistsException  extends Exception {
    private static final long serialVersionUID = -9079454849611061074L;

    public AttachmentExistsException() {
        super();
    }

    public AttachmentExistsException(final String message) {
        super(message);
    }
}
