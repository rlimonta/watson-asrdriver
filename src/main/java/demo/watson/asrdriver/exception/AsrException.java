package demo.watson.asrdriver.exception;

/**
 * @author Ricardo Limonta
 */
public class AsrException extends RuntimeException {

    public AsrException(String message) {
        super(message);
    }

    public AsrException(Throwable cause) {
        super(cause);
    }
}
