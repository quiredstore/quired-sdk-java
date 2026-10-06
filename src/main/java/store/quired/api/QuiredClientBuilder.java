package store.quired.api;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Objects;

public final class QuiredClientBuilder {

    private static final String DEFAULT_BASE_URL =
            "https://quired.store/api/v1";

    private String baseUrl = DEFAULT_BASE_URL;
    private String apiKey;
    private Duration timeout = Duration.ofSeconds(30);

    public QuiredClientBuilder baseUrl(String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl);
        return this;
    }

    public QuiredClientBuilder apiKey(String apiKey) {
        this.apiKey = Objects.requireNonNull(apiKey);
        return this;
    }

    public QuiredClientBuilder timeout(Duration timeout) {
        this.timeout = Objects.requireNonNull(timeout);
        return this;
    }

    public QuiredClient build() {
        if (apiKey.isBlank()) {
            throw new IllegalArgumentException("API key must not be blank");
        }

        if (baseUrl.isBlank()) {
            throw new IllegalArgumentException("Base URL must not be blank");
        }

        if (timeout.isNegative() || timeout.isZero()) {
            throw new IllegalArgumentException(
                    "Timeout must be greater than zero"
            );
        }

        HttpClient javaHttpClient = HttpClient.newBuilder()
                .connectTimeout(timeout)
                .build();

        QuiredHttpClient httpClient = new QuiredHttpClient(
                javaHttpClient,
                baseUrl,
                apiKey,
                timeout
        );

        return new QuiredClient(httpClient);
    }
}