package com.team3.services;

import com.team3.entities.Offer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExcelExportService {

    @Autowired
    private OfferService offerService;


    public ByteArrayInputStream exportOffersToExcel(LocalDateTime startDate, LocalDateTime endDate) {
        String[] columns = {"ID", "Candidate Email", "Approver Name", "Department", "Status", "Notes", "Contract Type", "Position", "Level", "Interview Info", "Recruiter Owner", "Basic Salary", "Contract Period From", "Contract Period To", "Due Date"};
        List<Offer> offers = offerService.getOffersByDateRange(startDate, endDate);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Offers");

            // Header row
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
            }

            // Data rows
            int rowIdx = 1;
            for (Offer offer : offers) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(offer.getOfferId());
                row.createCell(1).setCellValue(offer.getCandidate().getEmail());
                row.createCell(2).setCellValue(offer.getApprover().getFullName());
                row.createCell(3).setCellValue(offer.getDepartment());
                row.createCell(4).setCellValue(offer.getOfferStatus());
                row.createCell(5).setCellValue(offer.getNotes());
                row.createCell(6).setCellValue(offer.getContractType());
                row.createCell(7).setCellValue(offer.getPosition());
                row.createCell(8).setCellValue(offer.getLevel());
                row.createCell(9).setCellValue(offer.getInterviewInfo());
                row.createCell(10).setCellValue(offer.getRecruiterOwner());
                row.createCell(11).setCellValue(offer.getBasicSalary());
                row.createCell(12).setCellValue(offer.getContractPeriodFrom().toString());
                row.createCell(13).setCellValue(offer.getContractPeriodTo().toString());
                row.createCell(14).setCellValue(offer.getDueDate().toString());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to Excel file: " + e.getMessage());
        }
    }
}
