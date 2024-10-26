package com.team3.controllers.offer;

import com.team3.dtos.offer.OfferDTO;
import com.team3.entities.Candidate;
import com.team3.entities.InterviewSchedule;
import com.team3.entities.Offer;
import com.team3.entities.User;
import com.team3.services.CandidateService;
import com.team3.services.InterviewScheduleService;
import com.team3.services.OfferService;
import com.team3.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private UserService userService;

    @Autowired
    private InterviewScheduleService interviewScheduleService;

    @GetMapping("")
    public String listOffers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        List<Offer> allOffers = offerService.getAllOffers();
        List<String> departments = allOffers.stream()
                .map(Offer::getDepartment)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        Page<Offer> offerPage = offerService.searchOffers(search, department, status, pageable);

        model.addAttribute("offers", offerPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", offerPage.getTotalPages());
        model.addAttribute("totalOffers", offerPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("department", department);
        model.addAttribute("departments", departments);
        model.addAttribute("status", status);
        return "contents/offer/offer_list";
    }

    // add
    @GetMapping("/add")
    public String addOffer(Model model) {
        model.addAttribute("offer", new OfferDTO());
        List<Candidate> candidates = candidateService.getAllCandidatesNoBanned();
        List<User> managers = userService.getAllManagers();
        List<InterviewSchedule> interviewSchedules = interviewScheduleService.getAllInterviewSchedules();
        List<User> recruiters = userService.getAllRecruiters();
        model.addAttribute("candidates", candidates);
        model.addAttribute("managers", managers);
        model.addAttribute("interviewSchedules", interviewSchedules);
        model.addAttribute("recruiters", recruiters);
        return "contents/offer/offer_create";
    }

    @PostMapping("/add")
    public String saveOffer(@Valid @ModelAttribute("offer") OfferDTO offerDTO, BindingResult result,  Principal principal, Model model) {
        if (result.hasErrors()) {
            List<Candidate> candidates = candidateService.getAllCandidatesNoBanned();
            List<User> managers = userService.getAllManagers();
            List<InterviewSchedule> interviewSchedules = interviewScheduleService.getAllInterviewSchedules();
            List<User> recruiters = userService.getAllRecruiters();
            model.addAttribute("candidates", candidates);
            model.addAttribute("managers", managers);
            model.addAttribute("interviewSchedules", interviewSchedules);
            model.addAttribute("recruiters", recruiters);
            return "contents/offer/offer_create";
        }
        String username = principal.getName() != null ? principal.getName() : "admin";

        Offer offer = new Offer();
        offer.setModifiedBy(username);
        Candidate c = candidateService.getCandidateById(Long.parseLong(offerDTO.getCandidate()));
        User m = userService.getUser(Long.parseLong(offerDTO.getApprover()));
        offer.setCandidate(c);
        offer.setContractType(offerDTO.getContractType());
        offer.setPosition(offerDTO.getPosition());
        offer.setLevel(offerDTO.getLevel());
        offer.setApprover(m);
        offer.setDepartment(offerDTO.getDepartment());
        offer.setInterviewInfo(offerDTO.getInterviewNote());
        offer.setInterviewNote(offerDTO.getInterviewNote());
        offer.setRecruiterOwner(offerDTO.getRecruiterOwner());
        offer.setBasicSalary(Double.parseDouble(offerDTO.getBasicSalary()));
        offer.setNotes(offerDTO.getNotes());
        try {
            System.out.println(offerDTO.getContractPeriodFrom());
            LocalDate contractPeriodFrom = LocalDate.parse(offerDTO.getContractPeriodFrom(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate contractPeriodTo = LocalDate.parse(offerDTO.getContractPeriodTo(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            offer.setContractPeriodFrom(contractPeriodFrom);
            offer.setContractPeriodTo(contractPeriodTo);
            LocalDate dueDate = LocalDate.parse(offerDTO.getDueDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            offer.setDueDate(dueDate);
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Invalid date format.");
            return "contents/offer/offer_create";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid date format.");
            return "contents/offer/offer_create";
        }
        offerService.saveOffer(offer);
        return "redirect:/offers";
    }
}
