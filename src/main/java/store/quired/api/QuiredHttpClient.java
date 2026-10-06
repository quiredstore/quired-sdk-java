package store.quired.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import store.quired.api.exception.ForbiddenException;
import store.quired.api.exception.NotFoundException;
import store.quired.api.exception.QuiredApiException;
import store.quired.api.exception.RateLimitException;
import store.quired.api.exception.UnauthenticatedException;
import store.quired.api.exception.ValidationException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public final class QuiredHttpClient {

    private final HttpClient client;
    private final String baseUrl;
    private final String apiKey;
    private final Duration timeout;
    private final ObjectMapper objectMapper;

    QuiredHttpClient(
            HttpClient client,
            String baseUrl,
            String apiKey,
            Duration timeout
    ) {
        this.client = client;
        this.baseUrl = removeTrailingSlash(baseUrl);
        this.apiKey = apiKey;
        this.timeout = timeout;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    public <T> T get(String path, Class<T> responseType) {
        return send("GET", path, null, responseType);
    }

    public <T> T post(String path, Object body, Class<T> responseType) {
        return send("POST", path, body, responseType);
    }

    public <T> T put(String path, Object body, Class<T> responseType) {
        return send("PUT", path, body, responseType);
    }

    public void delete(String path) {
        send("DELETE", path, null, Void.class);
    }

    public <T> T patch(String path, Object body, Class<T> responseType) {
        return send("PATCH", path, body, responseType);
    }

    private <T> T send(
            String method,
            String path,
            Object body,
            Class<T> responseType
    ) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + normalizePath(path)))
                    .timeout(timeout)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "application/json");

            if (body != null) {
                String json = objectMapper.writeValueAsString(body);

                requestBuilder
                        .header("Content-Type", "application/json")
                        .method(
                                method,
                                HttpRequest.BodyPublishers.ofString(json)
                        );
            } else {
                requestBuilder.method(
                        method,
                        HttpRequest.BodyPublishers.noBody()
                );
            }

            HttpResponse<String> response =
                    client.send(
                            requestBuilder.build(),
                            HttpResponse.BodyHandlers.ofString()
                    );

            return handleResponse(response, responseType);

        } catch (IOException e) {
            throw new QuiredApiException(
                    "Failed to communicate with Quired API",
                    e
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new QuiredApiException(
                    "Request to Quired API was interrupted",
                    e
            );
        }
    }

    private <T> T handleResponse(
            HttpResponse<String> response,
            Class<T> responseType
    ) {
        int status = response.statusCode();
        String body = response.body();

        if (status >= 200 && status < 300) {
            if (responseType == Void.class || body == null || body.isBlank()) {
                return null;
            }

            try {
                return objectMapper.readValue(body, responseType);
            } catch (IOException e) {
                throw new QuiredApiException(
                        "Failed to parse Quired API response",
                        e
                );
            }
        }

        throw createException(status, body, response);
    }

    private RuntimeException createException(
            int status,
            String body,
            HttpResponse<String> response
    ) {
        String message = extractMessage(body);

        return switch (status) {
            case 401 -> new UnauthenticatedException(message);
            case 403 -> new ForbiddenException(message);
            case 404 -> new NotFoundException(message);
            case 422 -> new ValidationException(
                    message,
                    extractErrors(body)
            );
            case 429 -> new RateLimitException(
                    message,
                    retryAfter(response)
            );
            default -> new QuiredApiException(
                    status,
                    message
            );
        };
    }

    private String extractMessage(String body) {
        try {
            JsonNode json = objectMapper.readTree(body);

            if (json.has("message")) {
                return json.get("message").asText();
            }
        } catch (Exception ignored) {}

        return body == null || body.isBlank()
                ? "Unknown API error"
                : body;
    }

    private Map<String, String[]> extractErrors(String body) {
        try {
            JsonNode json = objectMapper.readTree(body);
            JsonNode errors = json.get("errors");

            if (errors instanceof ObjectNode objectNode) {
                return objectMapper.convertValue(
                        objectNode,
                        objectMapper.getTypeFactory()
                                .constructMapType(
                                        Map.class,
                                        String.class,
                                        String[].class
                                )
                );
            }
        } catch (Exception ignored) {
        }

        return Map.of();
    }

    private long retryAfter(HttpResponse<String> response) {
        return response.headers()
                .firstValue("Retry-After")
                .map(value -> {
                    try {
                        return Long.parseLong(value);
                    } catch (NumberFormatException e) {
                        return 0L;
                    }
                })
                .orElse(0L);
    }

    private static String removeTrailingSlash(String value) {
        if (value.endsWith("/")) {
            return value.substring(0, value.length() - 1);
        }

        return value;
    }

    private static String normalizePath(String path) {
        if (path.startsWith("/")) {
            return path;
        }

        return "/" + path;
    }
}
