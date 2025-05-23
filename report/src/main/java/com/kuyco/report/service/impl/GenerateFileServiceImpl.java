package com.kuyco.report.service.impl;

import com.kuyco.avro_schemas.TransactionEvent;
import com.kuyco.report.service.GenerateFileService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class GenerateFileServiceImpl implements GenerateFileService {

    @Override
    public byte[] generateExcel(TransactionEvent event) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Transaction ID");
        header.createCell(1).setCellValue("Customer ID");
        header.createCell(2).setCellValue("Customer Name");
        header.createCell(3).setCellValue("Item");
        header.createCell(4).setCellValue("Quantity");
        header.createCell(5).setCellValue("Total Price");
        header.createCell(6).setCellValue("Created At");

        Row data = sheet.createRow(1);
        data.createCell(0).setCellValue(event.getId());
        data.createCell(1).setCellValue(event.getCustomerId());
        data.createCell(2).setCellValue(event.getCustomerName());
        data.createCell(3).setCellValue(event.getItemName());
        data.createCell(4).setCellValue(event.getQuantity());
        data.createCell(5).setCellValue(event.getTotalPrice());
        data.createCell(6).setCellValue(event.getCreatedAt().toString());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }

    public String generateHtml(TransactionEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><style>")
                .append("table, th, td { border: 1px solid black; border-collapse: collapse; padding: 8px; }")
                .append("</style></head><body>");
        sb.append("<h2>Transaction Report</h2>");
        sb.append("<table><tr><th>Transaction ID</th><th>Customer ID</th><th>Customer Name</th><th>Item Name</th><th>Qty</th><th>Total Price</th><th>Created At</th></tr>");

        sb.append("<tr>")
                .append("<td>").append(event.getId()).append("</td>")
                .append("<td>").append(event.getCustomerId()).append("</td>")
                .append("<td>").append(event.getCustomerName()).append("</td>")
                .append("<td>").append(event.getItemName()).append("</td>")
                .append("<td>").append(event.getQuantity()).append("</td>")
                .append("<td>").append(event.getTotalPrice()).append("</td>")
                .append("<td>").append(event.getCreatedAt()).append("</td>")
                .append("</tr>");

        sb.append("</table></body></html>");
        return sb.toString();
    }

    @Override
    public byte[] generatePdf(TransactionEvent event) {
        String html = generateHtml(event);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("PDF Generation failed", e);
        }
    }
}
