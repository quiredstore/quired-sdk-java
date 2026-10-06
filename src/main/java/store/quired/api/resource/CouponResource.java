package store.quired.api.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import store.quired.api.QuiredHttpClient;
import store.quired.api.model.order.Coupon;
import store.quired.api.model.order.CouponRequest;
import store.quired.api.model.Page;

import java.util.List;
import java.util.Objects;

public final class CouponResource {

    private final QuiredHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public CouponResource(QuiredHttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    public Page<Coupon> list() {
        JsonNode response =
                httpClient.get("/coupons", JsonNode.class);

        return parsePage(response);
    }

    public Coupon create(CouponRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        JsonNode response =
                httpClient.post(
                        "/coupons",
                        request,
                        JsonNode.class
                );

        return parseData(response);
    }

    public void delete(long id) {
        httpClient.delete("/coupons/" + id);
    }

    private Coupon parseData(JsonNode response) {
        try {
            return objectMapper.treeToValue(
                    response.get("data"),
                    Coupon.class
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to deserialize coupon response",
                    e
            );
        }
    }

    private Page<Coupon> parsePage(JsonNode response) {
        try {
            List<Coupon> items = objectMapper.readerFor(
                    objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, Coupon.class)
            ).readValue(response.get("data"));

            JsonNode meta = response.get("meta");
            JsonNode links = response.get("links");

            return new Page<>(
                    items,
                    meta.get("current_page").asInt(),
                    meta.get("last_page").asInt(),
                    meta.get("per_page").asInt(),
                    meta.get("total").asLong(),
                    nullableText(links, "next"),
                    nullableText(links, "prev")
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to deserialize paginated coupons response",
                    e
            );
        }
    }

    private String nullableText(JsonNode node, String field) {
        JsonNode value = node.get(field);

        if (value == null || value.isNull()) {
            return null;
        }

        return value.asText();
    }
}
