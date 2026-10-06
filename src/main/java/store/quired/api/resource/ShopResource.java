package store.quired.api.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import store.quired.api.QuiredHttpClient;
import store.quired.api.model.Shop;

public final class ShopResource {

    private final QuiredHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ShopResource(QuiredHttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    public Shop get() {
        JsonNode response = httpClient.get("/shop", JsonNode.class);

        try {
            return objectMapper.treeToValue(
                    response.get("data"),
                    Shop.class
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to deserialize shop response",
                    e
            );
        }
    }
}
