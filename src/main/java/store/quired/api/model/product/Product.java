package store.quired.api.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public record Product(
        long id,
        String title,
        String slug,
        String description,
        String type,
        long price,
        String currency,

        @JsonProperty("min_quantity")
        int minQuantity,

        @JsonProperty("max_quantity")
        Integer maxQuantity,

        @JsonProperty("is_active")
        boolean active,

        Integer stock,

        List<ProductOption> options,
        List<ProductVariant> variants,

        String url,

        @JsonProperty("created_at")
        OffsetDateTime createdAt,

        @JsonProperty("updated_at")
        OffsetDateTime updatedAt
) {
}