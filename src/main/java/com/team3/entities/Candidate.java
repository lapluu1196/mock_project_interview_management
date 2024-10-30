package com.team3.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Candidates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Long candidateId;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", nullable = false, length = 50)
    private String gender;

    @Column(name = "cv", length = 255)
    private String cv;

    @Column(name = "current_position", nullable = false, length = 100)
    private String currentPosition;

    @Column(name = "skills", nullable = false, length = 255)
    private String skills;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "highest_education_level", length = 100)
    private String highestEducationLevel;

    @ManyToOne
    @JoinColumn(name = "recruiter_owner_id", nullable = false)
    private User recruiterOwner;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Add the relationship with InterviewSchedule
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InterviewSchedule> interviewSchedules;
}
