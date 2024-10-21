package com.team3.dtos.candidate;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDTO {
    private Long candidateId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private String cv;
    private String currentPosition;
    private String skills;
    private Integer yearsOfExperience;
    private String highestEducationLevel;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
