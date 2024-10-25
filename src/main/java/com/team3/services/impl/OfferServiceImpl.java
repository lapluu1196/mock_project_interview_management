package com.team3.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.team3.entities.Offer;
import com.team3.repositories.OfferRepository;
import com.team3.services.OfferService;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;

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
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }
}
