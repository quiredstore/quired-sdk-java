package store.quired.api.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record Coupon(
        long id,
        String code,
        String type,
        long value,
        @JsonProperty("product_id") Long productId,
        @JsonProperty("max_uses") Integer maxUses,
        int uses,
        @JsonProperty("expires_at") OffsetDateTime expiresAt,
        @JsonProperty("is_active") boolean active,
        @JsonProperty("created_at") OffsetDateTime createdAt
) {}