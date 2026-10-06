package store.quired.api.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ShippingAddress(
        String name,
        String line1,
        String line2,
        String city,
        String state,
        @JsonProperty("postal_code") String postalCode,
        String country,
        String phone
) {}