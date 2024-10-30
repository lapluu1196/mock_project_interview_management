package com.team3.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.team3.dtos.email.EmailDTO;
import com.team3.entities.Candidate;
import com.team3.payload.OfferStatus;
import com.team3.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.team3.entities.Offer;
import com.team3.repositories.OfferRepository;
import com.team3.services.OfferService;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public Page<Offer> getAllOffers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return offerRepository.findAll(pageable);
    }

    @Override
    public Offer getOfferById(Long id) {
        if (!offerRepository.findById(id).isPresent()) {
            throw new RuntimeException("Offer not found");
        }
        return offerRepository.findById(id).get();
    }

    @Override
    public void saveOffer(Offer offer) {
        offerRepository.save(offer);
    }

    public Page<Offer> searchOffers(String search, String department, String candidateStatus, Pageable pageable) {
        String searchParam = (search == null || search.trim().isEmpty()) ? null : search;
        String departmentParam = (department == null || department.trim().isEmpty()) ? null : department;
        String candidateStatusParam = (candidateStatus == null || candidateStatus.trim().isEmpty()) ? null
                : candidateStatus;

        return offerRepository.searchOffers(searchParam, departmentParam, candidateStatusParam, pageable);
    }

    @Override
    public void changeStatus(OfferStatus entity) {
        try {

            Offer offer = getOfferById(Long.parseLong(entity.getOfferId()));
            offer.setOfferStatus(entity.getStatus());
            saveOffer(offer);
        } catch (Exception e) {
            throw new RuntimeException("Offer not found");
        }
    }

    @Override
    public void updateCandidateStatus(OfferStatus entity) {
        try {
            Offer offer = getOfferById(Long.parseLong(entity.getOfferId()));
            offer.setOfferStatus(entity.getStatus());
            saveOffer(offer);
            Candidate candidate = candidateRepository.findById(Long.parseLong(entity.getCandidateId())).get();
            candidate.setStatus(entity.getStatus());
            candidateRepository.save(candidate);
        } catch (Exception e) {
            throw new RuntimeException("Offer not found");
        }
    }

    @Override
    public void updateStatus(Long id, String status) {
        Offer offer = getOfferById(id);
        offer.setOfferStatus(status);
        saveOffer(offer);
    }

    @Override
    public List<Offer> getOffersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return offerRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public List<Offer> getOffersDueToday() {
        LocalDate today = LocalDate.now();
        return offerRepository.findByDueDate(today);
    }
}
