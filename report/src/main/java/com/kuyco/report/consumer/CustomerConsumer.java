package com.kuyco.report.consumer;

import com.kuyco.avro_schemas.CustomerAvro;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerConsumer {

    @KafkaListener(
            topics = "${kafka.topic.customer-created}",
            groupId = "report-service"
    )
    public void listenCustomerCreated(ConsumerRecord<String, CustomerAvro> record) {
        CustomerAvro avro = record.value();
        log.info("listenCustomerCreated() customer consumer: {}", avro);
    }

}
