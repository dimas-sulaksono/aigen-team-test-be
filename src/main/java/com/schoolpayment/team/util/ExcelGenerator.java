package com.schoolpayment.team.util;

import com.schoolpayment.team.dto.response.ExcelResponse;
import com.schoolpayment.team.dto.response.PaymentResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelGenerator {

    public static byte[] generateExcel(List<ExcelResponse> payments) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Payments");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "ID", "Name", "User ID", "User Name",
                    "Student NIS", "Student Name", "Type",
                    "Amount", "Status", "Description", "Created At"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(getHeaderStyle(workbook));
            }

            // Data
            int rowNum = 1;
            for (ExcelResponse payment : payments) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(payment.getId());
                row.createCell(1).setCellValue(payment.getName());
                row.createCell(2).setCellValue(payment.getUserId());
                row.createCell(3).setCellValue(payment.getUserName());
                row.createCell(4).setCellValue(payment.getStudentNis());
                row.createCell(5).setCellValue(payment.getStudentName());
                row.createCell(6).setCellValue(payment.getType());
                row.createCell(7).setCellValue(payment.getAmount().doubleValue());
                row.createCell(8).setCellValue(payment.getStatus());
                row.createCell(9).setCellValue(payment.getDescription());
                row.createCell(10).setCellValue(payment.getCreatedAt().toString());
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private static CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
