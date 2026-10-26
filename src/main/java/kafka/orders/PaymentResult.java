package kafka.orders;

import java.time.Instant;

public record PaymentResult(
        String orderId,
        String status, // failed done
        String paymentId,
        String reason,
        Instant completedAt
) {
}
