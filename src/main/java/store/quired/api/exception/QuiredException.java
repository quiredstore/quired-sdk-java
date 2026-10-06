package store.quired.api.exception;

public class QuiredException extends RuntimeException {

    public QuiredException(String message) {
        super(message);
    }

    public QuiredException(String message, Throwable cause) {
        super(message, cause);
    }
}