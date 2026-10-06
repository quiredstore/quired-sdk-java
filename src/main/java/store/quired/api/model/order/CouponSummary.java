package store.quired.api.model.order;

public record CouponSummary(
        long id,
        String code,
        String type,
        long value
) {}
