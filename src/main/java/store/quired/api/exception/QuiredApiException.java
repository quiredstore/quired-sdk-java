package store.quired.api.exception;

public class QuiredApiException extends QuiredException {

    private final int statusCode;

    public QuiredApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
    }

    public QuiredApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int statusCode() {
        return statusCode;
    }
}
