package com.team3.controllers.candidate;

import com.team3.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.team3.services.CandidateService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String candidateList(@RequestParam(required = false) String search,
                                @RequestParam(required = false) String status,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {
        int size = 10;
        var pageable = PageRequest.of(page, size);

        var candidateDTOs = candidateService.filterCandidate(search, status, pageable);

        model.addAttribute("candidates", candidateDTOs);
        model.addAttribute("keyword", search);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", candidateDTOs.getTotalPages());
        model.addAttribute("totalCandidates", candidateDTOs.getTotalElements());

        return "contents/candidate/candidate-list";
    }

//    @GetMapping("/filter")
//    public String filterCandidate(@RequestParam(required = false) String search,
//                             @RequestParam(required = false) String status,
//                             @RequestParam(defaultValue = "0") int page,
//                             Model model) {
//        int size = 10;
//        var pageable = PageRequest.of(page, size);
//
//        var candidateDTOs = candidateService.filterCandidate(search, status, pageable);
//
//        model.addAttribute("candidates", candidateDTOs);
//        model.addAttribute("keyword", search);
//        model.addAttribute("status", status);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", candidateDTOs.getTotalPages());
//        model.addAttribute("totalCandidates", candidateDTOs.getTotalElements());
//
//        return "fragments/candidate-table :: candidateTable";
//    }
}
