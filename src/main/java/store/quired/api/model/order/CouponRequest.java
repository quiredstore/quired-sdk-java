package store.quired.api.model.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CouponRequest(
        String code,
        String type,
        Long value,
        @JsonProperty("product_id") Long productId,
        @JsonProperty("max_uses") Integer maxUses,
        @JsonProperty("expires_at") OffsetDateTime expiresAt,
        @JsonProperty("is_active") Boolean active
) {}
