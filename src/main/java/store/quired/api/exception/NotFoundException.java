package store.quired.api.exception;

public final class NotFoundException
        extends QuiredApiException {

    public NotFoundException(String message) {
        super(404, message);
    }
}
