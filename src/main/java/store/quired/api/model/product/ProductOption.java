package store.quired.api.model.product;

import java.util.List;

public record ProductOption(
        String name,
        List<String> values
) {
}