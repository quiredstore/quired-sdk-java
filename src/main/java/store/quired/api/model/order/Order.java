package store.quired.api.model.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public record Order(
        String id,
        String status,
        String email,
        long subtotal,
        long discount,
        long shipping,
        long total,
        @JsonProperty("refunded_amount") long refundedAmount,
        String currency,
        CouponSummary coupon,
        String gateway,
        @JsonProperty("gateway_reference") String gatewayReference,
        List<OrderItem> items,
        @JsonProperty("shipping_address") ShippingAddress shippingAddress,
        Fulfillment fulfillment,
        @JsonProperty("created_at") OffsetDateTime createdAt,
        @JsonProperty("paid_at") OffsetDateTime paidAt,
        @JsonProperty("completed_at") OffsetDateTime completedAt,
        @JsonProperty("refunded_at") OffsetDateTime refundedAt,
        Dispute dispute,
        boolean delivered
) {}