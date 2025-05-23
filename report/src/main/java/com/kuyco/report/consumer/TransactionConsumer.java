package com.kuyco.report.consumer;

import com.kuyco.avro_schemas.CustomerAvro;
import com.kuyco.avro_schemas.TransactionEvent;
import com.kuyco.report.service.GenerateFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class TransactionConsumer {

    private final GenerateFileService generateFileService;

    public TransactionConsumer(GenerateFileService generateFileService) {
        this.generateFileService = generateFileService;
    }

    @KafkaListener(
            topics = "${kafka.topic.transaction-created}",
            groupId = "report-service"
    )
    public void listenTransactionEvent(ConsumerRecord<String, TransactionEvent> record) {
        TransactionEvent avro = record.value();
        log.info("listenTransactionEvent() transaction consumer: {}", avro);
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            byte[] excel = generateFileService.generateExcel(avro);
            Files.write(Paths.get("report/generated/transactions_" + timestamp + "_" + avro.getCustomerName() + ".xlsx"), excel);
            log.info("Excel generated at report/generated/...");

            byte[] pdf = generateFileService.generatePdf(avro);
            Files.write(Paths.get("report/generated/transactions_" + timestamp + "_" + avro.getCustomerName() + ".pdf"), pdf);
            log.info("PDF generated at report/generated/...");

        } catch (Exception e) {
            log.error("Failed to consume transaction or generate report", e);
        }
    }

}
