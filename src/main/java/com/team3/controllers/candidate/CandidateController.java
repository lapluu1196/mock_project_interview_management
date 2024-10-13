package com.team3.controllers.candidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.team3.entities.Candidate;
import com.team3.services.CandidateService;

@Controller
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @GetMapping("/candidates")
    public String viewCandidates(Model model) {
        List<Candidate> candidates = candidateService.getAllCandidates();
        model.addAttribute("candidates", candidates);
        return "candidate-list";
    }
}
