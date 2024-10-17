package com.team3.dtos.candidate;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    // Default constructor
    public CandidateDTO() {}

    // Parameterized constructor
    public CandidateDTO(Long candidateId, String fullName, String email, String phoneNumber, String address,
                        LocalDate dateOfBirth, String gender, String cv, String currentPosition, String skills,
                        Integer yearsOfExperience, String highestEducationLevel, String status, String notes,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.candidateId = candidateId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.cv = cv;
        this.currentPosition = currentPosition;
        this.skills = skills;
        this.yearsOfExperience = yearsOfExperience;
        this.highestEducationLevel = highestEducationLevel;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getHighestEducationLevel() {
        return highestEducationLevel;
    }

    public void setHighestEducationLevel(String highestEducationLevel) {
        this.highestEducationLevel = highestEducationLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
