package com.team3.controllers.candidate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.team3.dtos.candidate.CandidateDTO;
import com.team3.services.CandidateService;

@Controller
@RequestMapping("/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    // UC05: View list of candidates with role-based and status filtering
    @GetMapping
    public String viewCandidateList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            Model model,
            @RequestParam("role") String role,
            @RequestParam(value = "page", defaultValue = "0") int page // For pagination
    ) {
        List<CandidateDTO> candidateList = candidateService.searchCandidates(keyword, status, role, page);
        model.addAttribute("candidates", candidateList);

        if (role.equals("HR") || role.equals("Recruiter")) {
            return "candidate_list_hr_recruiter";
        } else if (role.equals("Interviewer")) {
            return "candidate_list_interviewer";
        }

        return "redirect:/candidates";
    }

    // UC06: Create Candidate (HR/Recruiter only)
    @GetMapping("/add")
    public String showAddCandidateForm(Model model) {
        model.addAttribute("candidate", new CandidateDTO());
        return "add_candidate";
    }

    @PostMapping("/save")
    public String saveCandidate(@ModelAttribute("candidate") CandidateDTO candidateDTO) {
        candidateService.saveCandidate(candidateDTO);
        return "redirect:/candidates";
    }

    // UC07: View Candidate Information
    @GetMapping("/{id}")
    public String viewCandidateDetails(@PathVariable("id") Long id, Model model) {
        CandidateDTO candidateDTO = candidateService.getCandidateById(id);
        if (candidateDTO == null) {
            return "redirect:/candidates";
        }
        model.addAttribute("candidate", candidateDTO);
        return "view_candidate";
    }

    // UC08: Edit Candidate Information (HR/Recruiter only)
    @GetMapping("/edit/{id}")
    public String showEditCandidateForm(@PathVariable("id") Long id, Model model) {
        CandidateDTO candidateDTO = candidateService.getCandidateById(id);
        if (candidateDTO == null) {
            return "redirect:/candidates";
        }
        model.addAttribute("candidate", candidateDTO);
        return "edit_candidate";
    }

    @PostMapping("/update")
    public String updateCandidate(@ModelAttribute("candidate") CandidateDTO candidateDTO) {
        candidateService.updateCandidate(candidateDTO);
        return "redirect:/candidates";
    }

    // UC09: Delete Candidate (HR/Recruiter only)
    @GetMapping("/delete/{id}")
    public String deleteCandidate(@PathVariable("id") Long id) {
        candidateService.deleteCandidate(id);
        return "redirect:/candidates";
    }

    // UC10: Ban Candidate (HR/Recruiter only)
    @PostMapping("/ban/{id}")
    public String banCandidate(@PathVariable("id") Long id) {
        candidateService.banCandidate(id);
        return "redirect:/candidates";
    }

}
