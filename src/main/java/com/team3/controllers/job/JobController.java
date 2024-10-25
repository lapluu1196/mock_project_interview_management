package com.team3.controllers.job;

import ch.qos.logback.classic.Logger;
import com.team3.entities.Job;
import com.team3.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/list")
    public String getAllJobs(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(defaultValue = "0") int page,  // Thêm tham số cho trang hiện tại
            @RequestParam(defaultValue = "10") int size, // Thêm tham số cho kích thước trang
            Model model) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Job> jobPage = jobService.searchJobs(
                title != null && !title.isEmpty() ? title : null,
                status != null && !status.isEmpty() ? status : null,
                pageable
        );

        model.addAttribute("jobs", jobPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", jobPage.getTotalPages());

        return "jobs/list";
    }

    @GetMapping("/create")
    public String createJobForm(Model model) {
        Job job = new Job();
        model.addAttribute("job", job);
        return "jobs/create";
    }
    @PostMapping("/createjob")
    public String saveJob(@ModelAttribute("job") Job job) {
        job.setStatus("Draft");
        jobService.addJob(job);
        return "redirect:/jobs/list";
    }
    @GetMapping("/details/{id}")
    public String showJobDetails(@PathVariable Long id, Model model) {
        Job job = jobService.findById(id).orElse(null);
        if (job != null) {
            model.addAttribute("job", job);
            return "jobs/details";  // Tên view HTML (jobDetails.html)
        }
        return "redirect:/jobs/list"; // Quay lại danh sách nếu không tìm thấy job
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Job job = jobService.findById(id).orElse(null);
        if (job != null) {
            model.addAttribute("job", job);
            return "jobs/edit";  // Tên view HTML (jobDetails.html)
        }
        return "redirect:/jobs/list"; // Quay lại danh sách nếu không tìm thấy job
    }
    @PostMapping("/edit/{id}")
    public String updateJob(@PathVariable("id") Long id, @ModelAttribute("job") Job job) {
        job.setJobId(id);
        job.setStatus("Draft");
        jobService.save(job);
        return "redirect:/jobs/list";
    }
    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable("id") Long jobId, RedirectAttributes redirectAttributes) {
        try {
            jobService.deleteJobById(jobId);
            redirectAttributes.addFlashAttribute("message", "Job deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete the job.");
        }
        return "redirect:/jobs/list";
    }
}
