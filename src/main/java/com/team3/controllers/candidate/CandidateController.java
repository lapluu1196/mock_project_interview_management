
package com.team3.controllers.candidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.team3.dtos.candidate.CandidateDTO;
import com.team3.services.CandidateService;

import java.util.List;

@Controller
@RequestMapping("/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    // Display list of candidates with optional search and status filter
    @GetMapping
    public String viewCandidateList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            Model model) {

        // Fetch candidates based on search keyword and status if provided
        List<CandidateDTO> candidateList = candidateService.searchCandidates(keyword, status);
        model.addAttribute("candidates", candidateList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);

        return "candidate_list"; // Candidate list view (candidate_list.html)
    }
}
