package kafka.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {
    public static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final KafkaTemplate <String, OrderCreated> kafkaTemplate;
    private final OrdersRepository ordersRepository;

    public OrderEventPublisher(KafkaTemplate<String, OrderCreated> kafkaTemplate, OrdersRepository ordersRepository){
        this.kafkaTemplate = kafkaTemplate;
        this.ordersRepository = ordersRepository;
    }

    public void publish(OrderCreated event){
        kafkaTemplate.send(KafkaTopicConfig.ORDERS_TOPIC, event.orderId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null){
                        log.error("Failed to send order {}", event.orderId(), ex);
                    } else {
                        ordersRepository.registerOrder( event.orderId());
                        var meta = result.getRecordMetadata();
                        log.info("Sent {} -> partition {}, offset {}",
                                event.orderId(), meta.partition(), meta.offset());
                    }
                });
    }

}
