package store.quired.api.model.product;

import java.util.List;

public record ProductVariant(
        long id,
        String title,
        List<String> options,
        long price,
        Integer stock
) {
}