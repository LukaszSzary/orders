package kafka.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {
    public static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final KafkaTemplate <String, OrderCreated> kafkaTemplate;

    public OrderEventPublisher(KafkaTemplate<String, OrderCreated> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(OrderCreated event){
        kafkaTemplate.send(KafkaTopicConfig.ORDERS_TOPIC, event.orderId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null){
                        log.error("Nie udało się wysłać zamówienia {}", event.orderId(), ex);
                    } else {
                        var meta = result.getRecordMetadata();
                        log.info("Wysłano {} -> partycja {}, offset {}",
                                event.orderId(), meta.partition(), meta.offset());
                    }
                });
    }

}
