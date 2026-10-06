package store.quired.api.exception;

public final class RateLimitException
        extends QuiredApiException {

    private final long retryAfterSeconds;

    public RateLimitException(
            String message,
            long retryAfterSeconds
    ) {
        super(429, message);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long retryAfterSeconds() {
        return retryAfterSeconds;
    }
}
