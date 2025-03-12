package com.team3.services.impl;

import com.team3.dtos.offer.OfferListDTO;
import com.team3.entities.Offer;
import com.team3.repositories.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.team3.services.OfferService;

@Service
public class OfferServiceImpl implements OfferService {
    @Autowired
    private OfferRepository offerRepository;

    @Override
    public Page<OfferListDTO> searchAll(String search, String department, String status, Pageable pageable) {
        var result = offerRepository.searchAll(search, department, status, pageable);
        return result.map(offer -> {
            var offerListDTO = new OfferListDTO();
            offerListDTO.setOfferId(offer.getOfferId());
            offerListDTO.setCandidateName(offer.getCandidate().getFullName());
            offerListDTO.setCandidateEmail(offer.getCandidate().getEmail());
            offerListDTO.setApproverName(offer.getApprover().getFullName());
            offerListDTO.setDepartment(offer.getDepartment());
            offerListDTO.setNotes(offer.getNotes());
            offerListDTO.setStatus(offer.getOfferStatus());
            return offerListDTO;
        });
    }

}
