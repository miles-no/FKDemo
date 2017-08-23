package no.fjordkraft.im.exceptions;

/**
 * Created by bhavi on 5/2/2017.
 */
public class FileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileNotFoundException() {
    }

    public FileNotFoundException(String msg) {
        super(msg);
    }

    public FileNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

}