package store.quired.api.model.product;

public record ProductStock(
        int available,
        int sold
) {
}