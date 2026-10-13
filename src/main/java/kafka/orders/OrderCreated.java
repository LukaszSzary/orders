package kafka.orders;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderCreated(
        String orderId,
        String customerId,
        String productId,
        int quality,
        BigDecimal totalAmount,
        Instant createdAt) {
}
