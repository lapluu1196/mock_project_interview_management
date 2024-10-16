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

    public Long candidate;
    public String position;
    public String approver;
    public String job;
    public String contractPeriodFrom;
    public String contractPeriodTo;
    public String contractType;
    public String level;
    public String department;
    public String recruiterOwner;
    public String dueDate;
    public String basicSalary;
    public String notes;
}
