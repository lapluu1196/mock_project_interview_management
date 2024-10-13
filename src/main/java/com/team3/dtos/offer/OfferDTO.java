package com.team3.dtos.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferDTO {

    private Long offerId;

    private Long candidateId;

    private Long jobId;

    private Long approverId;

    private String contractType;

    private String level;

    private String department;

    private String interviewInfo;

    private String recruiterOwner;

    private LocalDate contractPeriodFrom;

    private LocalDate contractPeriodTo;

    private Double basicSalary;

    private String offerStatus;

    private LocalDate dueDate;

    private String notes;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}
