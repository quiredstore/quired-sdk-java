package store.quired.api;

import store.quired.api.resource.CouponResource;
import store.quired.api.resource.OrderResource;
import store.quired.api.resource.ProductResource;
import store.quired.api.resource.ShopResource;

import java.util.Objects;

public final class QuiredClient {

    @SuppressWarnings("unused")
    private final QuiredHttpClient httpClient;

    private final ShopResource shop;
    private final ProductResource products;
    private final OrderResource orders;
    private final CouponResource coupons;

    QuiredClient(QuiredHttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient);

        this.shop = new ShopResource(httpClient);
        this.products = new ProductResource(httpClient);
        this.orders = new OrderResource(httpClient);
        this.coupons = new CouponResource(httpClient);
    }

    public static QuiredClientBuilder builder() {
        return new QuiredClientBuilder();
    }

    public ShopResource shop() {
        return shop;
    }

    public ProductResource products() {
        return products;
    }

    public OrderResource orders() {
        return orders;
    }

    public CouponResource coupons() {
        return coupons;
    }
}