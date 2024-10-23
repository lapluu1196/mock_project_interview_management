package com.team3.controllers.interviewschedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team3.services.InterviewScheduleService;

@RestController
@RequestMapping("/api/interview-schedule")
public class InterviewScheduleAPIController {

    @Autowired
    private InterviewScheduleService interviewScheduleService;

    @PostMapping("/cancel/{id}")
    @Transactional
    public ResponseEntity<String> cancelInterview(@PathVariable Long id) {
        boolean isCancelled = interviewScheduleService.cancelStatusById(id); // Gọi phương thức để hủy lịch phỏng vấn
        if (isCancelled) {
            return ResponseEntity.ok("Interview has been successfully canceled!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Interview not found.");
        }
    }

}
