package no.fjordkraft.im.exceptions;

/**
 * Created by bhavi on 5/23/2017.
 */
public class PreprocessorException extends RuntimeException {

    public PreprocessorException(String message) {
        super(message);
    }

    public PreprocessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public PreprocessorException(Throwable cause) {
        super(cause);
    }
}
