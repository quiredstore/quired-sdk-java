package store.quired.api.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderItem(
        @JsonProperty("product_id") long productId,
        String title,
        String variant,
        String slug,
        String type,
        @JsonProperty("unit_price") long unitPrice,
        int quantity,
        long total
) {}
