package store.quired.api.exception;

public final class ForbiddenException
        extends QuiredApiException {

    public ForbiddenException(String message) {
        super(403, message);
    }
}
