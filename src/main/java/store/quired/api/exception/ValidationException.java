package store.quired.api.exception;

import java.util.Map;

public final class ValidationException
        extends QuiredApiException {

    private final Map<String, String[]> errors;

    public ValidationException(
            String message,
            Map<String, String[]> errors
    ) {
        super(422, message);
        this.errors = Map.copyOf(errors);
    }

    public Map<String, String[]> errors() {
        return errors;
    }
}
