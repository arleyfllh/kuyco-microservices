package com.kuyco.api.producer;

import com.kuyco.api.model.Customer;
import com.kuyco.avro_schemas.CustomerAvro;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
@Slf4j
public class CustomerProducer {

    private final KafkaTemplate<String, CustomerAvro> kafkaTemplate;

    @Value("${kafka.topic.customer-created}")
    private String topic;

    public CustomerProducer(KafkaTemplate<String, CustomerAvro> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCustomerCreated(Customer customer) {
        try {
            CustomerAvro avro = CustomerAvro.newBuilder()
                    .setId(customer.getId())
                    .setName(customer.getName())
                    .setBalance(customer.getBalance())
                    .setEmail(customer.getEmail())
                    .setCreatedAt(customer.getCreatedAt()
                            .atZone(ZoneId.of("Asia/Jakarta"))
                            .toInstant())
                    .build();
            kafkaTemplate.send(topic, avro);
            log.info("sendCustomerCreated() Send created customer to Kafka: {}", customer);
        } catch (Exception e) {
            log.error("sendCustomerCreated() Error sending created customer: {}", e.getMessage());
        }
    }
}
