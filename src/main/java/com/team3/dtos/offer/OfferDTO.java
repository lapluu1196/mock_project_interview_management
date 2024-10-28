package com.team3.dtos.offer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferDTO {

    @NotNull(message = "Candidate is required")
    private String candidate;

    @NotBlank(message = "Position is required")
    @Size(max = 255, message = "Position must be less than 255 characters")
    private String position;

    @NotBlank(message = "Approver is required")
    private String approver;

    @NotBlank(message = "Job is required")
    private String job;

    @NotNull(message = "Contract period from is required")
    private String contractPeriodFrom;

    @NotNull(message = "Contract period to is required")
    private String contractPeriodTo;

    @NotBlank(message = "Contract type is required")
    @Size(max = 50, message = "Contract type must be less than 50 characters")
    private String contractType;

    @NotBlank(message = "Level is required")
    @Size(max = 255, message = "Level must be less than 255 characters")
    private String level;

    @NotBlank(message = "Department is required")
    @Size(max = 255, message = "Department must be less than 255 characters")
    private String department;

    @NotBlank(message = "Recruiter owner is required")
    @Size(max = 255, message = "Recruiter owner must be less than 255 characters")
    private String recruiterOwner;

    @NotNull(message = "Due date is required")
    private String dueDate;

    @NotNull(message = "Basic salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Basic salary must be greater than 0")
    private String basicSalary;

    private String notes;

    private String modifiedBy;

    private String interviewNote;

    private String offerStatus;
}
