package com.team3.services.impl;

import com.team3.entities.Candidate;
import com.team3.dtos.candidate.CandidateDTO;
import com.team3.services.CandidateService;
import com.team3.repositories.CandidateRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public List<CandidateDTO> searchCandidates(String keyword, String status) {
        List<Candidate> candidates;

        // If both keyword and status are provided
        if ((keyword != null && !keyword.isEmpty()) && (status != null && !status.isEmpty())) {
            candidates = candidateRepository.findByKeywordAndStatus(keyword, status);
        }
        // If only keyword is provided
        else if (keyword != null && !keyword.isEmpty()) {
            candidates = candidateRepository.findByKeyword(keyword);
        }
        // If only status is provided
        else if (status != null && !status.isEmpty()) {
            candidates = candidateRepository.findByStatus(status);
        }
        // If neither keyword nor status are provided, return all candidates
        else {
            candidates = candidateRepository.findAll();
        }

        // Convert Candidate entity to CandidateDTO for the controller
        return candidates.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Helper method to convert Candidate entity to DTO
    private CandidateDTO convertToDTO(Candidate candidate) {
        return new CandidateDTO(
                candidate.getId(),
                candidate.getName(),
                candidate.getEmail(),
                candidate.getPhoneNo(),
                candidate.getCurrentPosition(),
                candidate.getOwnerHR(),
                candidate.getStatus()
        );
    }
}