package store.quired.api.model;

import java.util.Map;

public record ValidationErrors(
        Map<String, String[]> errors
) {
}
