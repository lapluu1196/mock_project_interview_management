package com.team3.controllers.offer;

import com.team3.payload.OfferStatus;
import com.team3.services.ExcelExportService;
import com.team3.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/offers")
public class OfferApiController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private ExcelExportService excelExportService;

    @PutMapping("/change-status")
    public String postMethodName(@RequestBody OfferStatus entity) {
        // offerService.changeStatus(entity);
        offerService.changeStatus(entity);
        return "success";
    }

    @PutMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestBody String status) {
        System.out.println("đây là status:" + status);
        offerService.updateStatus(id, status);
        return "success";
    }

    @PutMapping("/update-status")
    public String updateCandidateStatus(@RequestBody OfferStatus entity) {
        offerService.updateCandidateStatus(entity);
        return "success";
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportOffersToExcel(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr + "T00:00:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr + "T23:59:59", formatter);
        ByteArrayInputStream in = excelExportService.exportOffersToExcel(startDate, endDate);
        String fileName = "Offer-" + startDateStr + "_" + endDateStr + ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + fileName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }
}
