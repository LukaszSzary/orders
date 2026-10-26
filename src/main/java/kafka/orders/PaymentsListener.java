package kafka.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PaymentsListener {
    public static final Logger log = LoggerFactory.getLogger(PaymentsListener.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProcessedPaymentResultsRepository processedPaymentResultsRepository;
    private final OrdersRepository ordersRepository;

    public PaymentsListener(KafkaTemplate<String, Object> kafkaTemplate, ProcessedPaymentResultsRepository processedPaymentResultsRepository, OrdersRepository ordersRepository){
        this.kafkaTemplate = kafkaTemplate;
        this.processedPaymentResultsRepository = processedPaymentResultsRepository;
        this.ordersRepository = ordersRepository;
    }

    @KafkaListener(topics ="payments")
    @Transactional
    public void onPaymentResult(PaymentResult paymentResult) throws Exception{
        if(!processedPaymentResultsRepository.markProcessed(paymentResult.paymentId())){
            log.info("Skipping duplicate order {}", paymentResult.paymentId());
            return;
        }

        String status = paymentResult.status().equals("failed") ? "CANCELLED" : "CONFIRMED";
        if(ordersRepository.changePaymentStatus(paymentResult.orderId(), status)){
            log.info("Failed to update payment status of order {}", paymentResult.orderId());
            return;
        }else{
            log.info("Succeeded to update payment status of order {}", paymentResult.orderId());
        }

        switch (ordersRepository.getOrderStatus(paymentResult.orderId())){
            case "CANCELLED":
                // TO DO send event order-status canceled
                log.info("Order: {} in CANCELLED",paymentResult.orderId());
                break;
            case "CONFIRMED":
                // TO DO send event order-status CONFIRMED
                log.info("Order: {} in CONFIRMED",paymentResult.orderId());
                break;
            case "PENDING":
                log.info("Order: {} in PENDING",paymentResult.orderId());
                break;

        }


    }
}
