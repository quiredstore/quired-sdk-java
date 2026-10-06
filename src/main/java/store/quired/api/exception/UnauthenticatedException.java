package store.quired.api.exception;

public final class UnauthenticatedException
        extends QuiredApiException {

    public UnauthenticatedException(String message) {
        super(401, message);
    }
}
