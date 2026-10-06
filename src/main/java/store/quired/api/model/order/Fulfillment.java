package store.quired.api.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record Fulfillment(
        String status,
        @JsonProperty("tracking_number") String trackingNumber,
        @JsonProperty("tracking_url") String trackingUrl,
        @JsonProperty("shipped_at") OffsetDateTime shippedAt
) {}