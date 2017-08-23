package no.fjordkraft.im.exceptions;

/**
 * Created by miles on 7/7/2017.
 */
public class PDFGeneratorException extends RuntimeException {

    public PDFGeneratorException(String message) {
        super(message);
    }

    public PDFGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public PDFGeneratorException(Throwable cause) {
        super(cause);
    }
}
