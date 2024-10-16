package com.team3.controllers.offer;

import com.team3.entities.Offer;
import com.team3.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

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
}
