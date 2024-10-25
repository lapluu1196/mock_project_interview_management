package com.team3.controllers.candidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.team3.dtos.candidate.CandidateDTO;
import com.team3.services.CandidateService;
import com.team3.services.UserService;

import java.util.List;

@Controller
@RequestMapping("/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private UserService userService;

    // UC05: View Candidate List based on UserID (interviewer can only see assigned candidates)
    @GetMapping
    public String viewCandidateList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            Model model,
            @RequestParam("userID") Long userID,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        List<CandidateDTO> candidateList = candidateService.searchCandidates(keyword, status, userID, page);
        model.addAttribute("candidates", candidateList);

        // Determine user view type by their role
        String userRole = userService.getUserRole(userID);
        return userRole.equals("Interviewer") ? "candidate_list_interviewer" : "candidate_list_hr";
    }

    // UC06: Create Candidate (Only for Recruiters, Managers, Admins)
    @GetMapping("/add")
    public String showAddCandidateForm(@RequestParam("userID") Long userID, Model model) {
        if (!userService.isAuthorized(userID, "HR", "Recruiter", "Admin")) {
            return "redirect:/candidates";
        }
        model.addAttribute("candidate", new CandidateDTO());
        return "add_candidate";
    }

    @PostMapping("/save")
    public String saveCandidate(@ModelAttribute("candidate") CandidateDTO candidateDTO, Model model) {
        try {
            candidateService.saveCandidate(candidateDTO);
            return "redirect:/candidates";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "add_candidate";
        }
    }

    // UC07: View Candidate Details
    @GetMapping("/{id}")
    public String viewCandidateDetails(@PathVariable("id") Long id, Model model, @RequestParam("userID") Long userID) {
        CandidateDTO candidateDTO = candidateService.getCandidateById(id);
        if (candidateDTO == null) {
            return "redirect:/candidates";
        }

        String userRole = userService.getUserRole(userID);
        model.addAttribute("candidate", candidateDTO);
        return userRole.equals("Interviewer") ? "view_candidate_interviewer" : "view_candidate";
    }

    // UC08: Edit Candidate (Only for HR, Recruiters, Admins)
    @GetMapping("/edit/{id}")
    public String showEditCandidateForm(@PathVariable("id") Long id, Model model, @RequestParam("userID") Long userID) {
        if (!userService.isAuthorized(userID, "HR", "Recruiter", "Admin")) {
            return "redirect:/candidates";
        }

        CandidateDTO candidateDTO = candidateService.getCandidateById(id);
        if (candidateDTO == null) {
            return "redirect:/candidates";
        }
        model.addAttribute("candidate", candidateDTO);
        return "edit_candidate";
    }

    @PostMapping("/update")
    public String updateCandidate(@ModelAttribute("candidate") CandidateDTO candidateDTO, Model model) {
        try {
            candidateService.updateCandidate(candidateDTO);
            return "redirect:/candidates";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "edit_candidate";
        }
    }

    // UC09: Delete Candidate (Only for HR, Recruiters, Admins, and if status is "Open")
    @GetMapping("/delete/{id}")
    public String deleteCandidate(@PathVariable("id") Long id, @RequestParam("userID") Long userID) {
        if (!userService.isAuthorized(userID, "HR", "Recruiter", "Admin")) {
            return "redirect:/candidates";
        }
        candidateService.deleteCandidate(id);
        return "redirect:/candidates";
    }

    // UC10: Ban Candidate (Only for HR, Recruiters, Admins)
    @PostMapping("/ban/{id}")
    public String banCandidate(@PathVariable("id") Long id, @RequestParam("userID") Long userID) {
        if (!userService.isAuthorized(userID, "HR", "Recruiter", "Admin")) {
            return "redirect:/candidates";
        }
        candidateService.banCandidate(id);
        return "redirect:/candidates";
    }
}
