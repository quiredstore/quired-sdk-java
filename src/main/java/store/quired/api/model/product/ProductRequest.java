package store.quired.api.model.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductRequest(
        String title,
        String type,
        Long price,
        String slug,
        String description,

        @JsonProperty("min_quantity")
        Integer minQuantity,

        @JsonProperty("max_quantity")
        Integer maxQuantity,

        @JsonProperty("service_instructions")
        String serviceInstructions,

        @JsonProperty("is_active")
        Boolean active
) {
}