package com.team3.payload;

import lombok.Data;

@Data
public class OfferStatus {
    private String offerId;
    private String status;
    private String candidateId;
}
