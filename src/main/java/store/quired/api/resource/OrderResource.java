package store.quired.api.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import store.quired.api.QuiredHttpClient;
import store.quired.api.model.order.Order;
import store.quired.api.model.Page;

import java.util.List;
import java.util.Objects;

public final class OrderResource {

    private final QuiredHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OrderResource(QuiredHttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    public Page<Order> list() {
        return list(null, null, null, null);
    }

    public Page<Order> list(
            String status,
            String email,
            Long productId,
            Integer perPage
    ) {
        StringBuilder path = new StringBuilder("/orders");
        boolean hasQuery = false;

        if (status != null) {
            path.append("?status=").append(status);
            hasQuery = true;
        }

        if (email != null) {
            path.append(hasQuery ? "&" : "?")
                    .append("email=")
                    .append(email);
            hasQuery = true;
        }

        if (productId != null) {
            path.append(hasQuery ? "&" : "?")
                    .append("product_id=")
                    .append(productId);
            hasQuery = true;
        }

        if (perPage != null) {
            path.append(hasQuery ? "&" : "?")
                    .append("per_page=")
                    .append(perPage);
        }

        JsonNode response =
                httpClient.get(path.toString(), JsonNode.class);

        return parsePage(response);
    }

    public Order get(String id) {
        Objects.requireNonNull(id, "id must not be null");

        JsonNode response =
                httpClient.get("/orders/" + id, JsonNode.class);

        return parseData(response);
    }

    private Order parseData(JsonNode response) {
        try {
            return objectMapper.treeToValue(
                    response.get("data"),
                    Order.class
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to deserialize order response",
                    e
            );
        }
    }

    private Page<Order> parsePage(JsonNode response) {
        try {
            List<Order> items = objectMapper.readerFor(
                    objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, Order.class)
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
                    "Failed to deserialize paginated orders response",
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
