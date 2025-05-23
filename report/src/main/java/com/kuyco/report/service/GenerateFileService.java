package com.kuyco.report.service;

import com.kuyco.avro_schemas.TransactionEvent;

import java.io.File;

public interface GenerateFileService {
    byte[] generateExcel(TransactionEvent event) throws Exception;

    byte[] generatePdf(TransactionEvent event);
}
