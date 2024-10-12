package com.team3.dtos.candidate;

import java.io.Serializable;

public class CandidateDTO implements Serializable {
    
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String currentPosition;
    private String ownerHR;
    private String status;

    // Default constructor
    public CandidateDTO() {
    }

    // Parameterized constructor
    public CandidateDTO(Long id, String name, String email, String phoneNumber, String currentPosition, String ownerHR, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.currentPosition = currentPosition;
        this.ownerHR = ownerHR;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getOwnerHR() {
        return ownerHR;
    }

    public void setOwnerHR(String ownerHR) {
        this.ownerHR = ownerHR;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CandidateDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", currentPosition='" + currentPosition + '\'' +
                ", ownerHR='" + ownerHR + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}