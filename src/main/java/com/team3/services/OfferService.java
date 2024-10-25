package com.team3.services;

import com.team3.entities.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OfferService {
    Page<Offer> getAllOffers(int pageNumber, int pageSize);

    Offer getOfferById(Long id);

    void saveOffer(Offer offer);

    // get all offers
    List<Offer> getAllOffers();

    Page<Offer> searchOffers(String search, String department, String status, Pageable pageable);
}
