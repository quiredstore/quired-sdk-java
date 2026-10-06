package store.quired.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Shop(
        long id,
        String name,
        String slug,
        String url,
        String currency,

        @JsonProperty("support_email")
        String supportEmail
) {
}