package store.quired.api.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import store.quired.api.QuiredHttpClient;
import store.quired.api.model.Page;
import store.quired.api.model.product.*;

import java.util.List;
import java.util.Objects;

public final class ProductResource {

    private final QuiredHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ProductResource(QuiredHttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    public Page<Product> list() {
        return list(null, null);
    }

    public Page<Product> list(Boolean active, Integer perPage) {
        StringBuilder path = new StringBuilder("/products");

        boolean hasQuery = false;

        if (active != null) {
            path.append("?active=").append(active);
            hasQuery = true;
        }

        if (perPage != null) {
            path.append(hasQuery ? "&" : "?")
                    .append("per_page=")
                    .append(perPage);
        }

        JsonNode response =
                httpClient.get(path.toString(), JsonNode.class);

        return parsePage(response, Product.class);
    }

    public Product get(long id) {
        JsonNode response =
                httpClient.get(
                        "/products/" + id,
                        JsonNode.class
                );

        return parseData(response, Product.class);
    }

    public Product create(
            ProductRequest request
    ) {
        JsonNode response =
                httpClient.post(
                        "/products",
                        request,
                        JsonNode.class
                );

        return parseData(response, Product.class);
    }

    public Product update(
            long id,
            ProductRequest request
    ) {
        JsonNode response =
                httpClient.patch(
                        "/products/" + id,
                        request,
                        JsonNode.class
                );

        return parseData(response, Product.class);
    }

    public void delete(long id) {
        httpClient.delete("/products/" + id);
    }

    public ProductStock stock(long id) {
        JsonNode response =
                httpClient.get(
                        "/products/" + id + "/stock",
                        JsonNode.class
                );

        return parseData(response, ProductStock.class);
    }

    public AddLicenseKeysResponse addLicenseKeys(
            long id,
            AddLicenseKeysRequest request
    ) {
        JsonNode response =
                httpClient.post(
                        "/products/" + id + "/stock",
                        request,
                        JsonNode.class
                );

        return parseData(response, AddLicenseKeysResponse.class);
    }

    private <T> T parseData(
            JsonNode response,
            Class<T> type
    ) {
        try {
            return objectMapper.treeToValue(
                    response.get("data"),
                    type
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to deserialize Quired API response",
                    e
            );
        }
    }

    private <T> Page<T> parsePage(
            JsonNode response,
            Class<T> type
    ) {
        try {
            List<T> items = objectMapper.readerFor(
                    objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, type)
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
                    "Failed to deserialize paginated Quired API response",
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