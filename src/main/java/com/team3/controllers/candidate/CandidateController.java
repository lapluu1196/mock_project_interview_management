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

    // @Autowired
    // private CandidateService candidateService;

    // // View list of candidates with optional search by keyword and status
    // @GetMapping
    // public String viewCandidateList(@RequestParam(value = "keyword", required = false) String keyword,
    //                                 @RequestParam(value = "status", required = false) String status,
    //                                 Model model) {
    //     List<CandidateDTO> candidateList = candidateService.searchCandidates(keyword, status);
    //     model.addAttribute("candidates", candidateList);
    //     return "candidate_list"; // Candidate list view
    // }

    // // Show form to add a new candidate
    // @GetMapping("/add")
    // public String showAddCandidateForm(Model model) {
    //     model.addAttribute("candidate", new CandidateDTO());
    //     return "add_candidate"; // Form for adding a candidate
    // }

    // // Save a new candidate
    // @PostMapping("/save")
    // public String saveCandidate(@ModelAttribute("candidate") CandidateDTO candidateDTO) {
    //     candidateService.saveCandidate(candidateDTO);
    //     return "redirect:/candidates"; // Redirect to candidate list
    // }

    // // Show form to edit an existing candidate
    // @GetMapping("/edit/{id}")
    // public String showEditCandidateForm(@PathVariable("id") Long id, Model model) {
    //     CandidateDTO candidateDTO = candidateService.getCandidateById(id);
    //     model.addAttribute("candidate", candidateDTO);
    //     return "edit_candidate"; // Form for editing a candidate
    // }

    // // Update an existing candidate
    // @PostMapping("/update")
    // public String updateCandidate(@ModelAttribute("candidate") CandidateDTO candidateDTO) {
    //     candidateService.updateCandidate(candidateDTO);
    //     return "redirect:/candidates"; // Redirect to candidate list
    // }

    // // Delete a candidate
    // @GetMapping("/delete/{id}")
    // public String deleteCandidate(@PathVariable("id") Long id) {
    //     candidateService.deleteCandidate(id);
    //     return "redirect:/candidates";
    // }
}
