package kafka.orders;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderStatusChanged(
        // unique per event, for dedup later
         String orderId,
         String status,         // "CONFIRMED" or "CANCELLED"
         Instant timestamp) {}