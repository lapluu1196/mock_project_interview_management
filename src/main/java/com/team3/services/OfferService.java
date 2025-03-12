package com.team3.services;

import com.team3.dtos.offer.OfferListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OfferService {
    Page<OfferListDTO> searchAll(String search, String department, String status, Pageable pageable);
}
