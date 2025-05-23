package com.kuyco.api.producer;

import com.kuyco.api.model.Transaction;
import com.kuyco.avro_schemas.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
@Slf4j
public class TransactionProducer {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    @Value("${kafka.topic.transaction-created}")
    private String topic;

    public TransactionProducer(KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransactionCreated(Transaction transaction) {
        try {
            TransactionEvent event = TransactionEvent.newBuilder()
                    .setId(transaction.getId())
                    .setCustomerId(transaction.getCustomer().getId())
                    .setCustomerName(transaction.getCustomer().getName())
                    .setItemId(transaction.getItem().getId())
                    .setItemName(transaction.getItem().getName())
                    .setQuantity(transaction.getQuantity())
                    .setTotalPrice(transaction.getTotalPrice())
                    .setCreatedAt(transaction.getCreatedAt()
                            .atZone(ZoneId.of("Asia/Jakarta"))
                            .toInstant())
                    .build();

            kafkaTemplate.send(topic, event);
            log.info("sendTransaction() Send transaction event to Kafka: {}", transaction);
        } catch (Exception e) {
            log.error("sendTransaction() Error sending transaction event: {}", e.getMessage());
        }
    }
}
