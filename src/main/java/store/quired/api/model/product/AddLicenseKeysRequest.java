package store.quired.api.model.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AddLicenseKeysRequest(
        List<String> items,

        @JsonProperty("skip_duplicates")
        Boolean skipDuplicates
) {
}