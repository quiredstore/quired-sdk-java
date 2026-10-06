package store.quired.api.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record Dispute(
        String status,
        @JsonProperty("opened_at") OffsetDateTime openedAt
) {}
