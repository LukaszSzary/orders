package kafka.orders;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

        var event = new OrderCreated(
                orderId,
                request.customerId(),
                request.productId(),
                request.quantity(),
                request.totalAmount(),
                Instant.now());

        publisher.publish(event);
        return orderId;
    }
}
