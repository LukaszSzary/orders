package kafka.orders;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import kafka.events.OrderCreated;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    public record CreatedOrderRequest(
            String customerId,
            String productId,
            int quantity,
            BigDecimal totalAmount){}

    private final OrderEventPublisher publisher;

    public OrderController(OrderEventPublisher publisher){
        this.publisher = publisher;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String createdOrder(@RequestBody CreatedOrderRequest request) {
        String orderId = UUID.randomUUID().toString();

        var event = OrderCreated.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setOrderId(orderId)
                .setCustomerId(request.customerId())
                .setProductId(request.productId())
                .setQuantity(request.quantity())
                .setTotalAmount(request.totalAmount().setScale(2, RoundingMode.HALF_UP))
                .setCreatedAt(Instant.now())
                .build();

        publisher.publish(event);
        return orderId;
    }
}
