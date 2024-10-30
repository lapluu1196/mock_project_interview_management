package com.team3.services;

import com.team3.entities.Offer;
import com.team3.payload.OfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OfferService {
    Page<Offer> getAllOffers(int pageNumber, int pageSize);

    Offer getOfferById(Long id);

    void saveOffer(Offer offer);
    List<Offer> getOffersDueToday();

    // get all offers
    List<Offer> getAllOffers();

    Page<Offer> searchOffers(String search, String department, String status, Pageable pageable);

    void changeStatus(OfferStatus entity);

    void updateCandidateStatus(OfferStatus entity);

    void updateStatus(Long id, String status);

    List<Offer> getOffersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
