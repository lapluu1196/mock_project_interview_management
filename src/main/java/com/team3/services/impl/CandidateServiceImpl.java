package com.team3.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team3.dtos.candidate.CandidateDTO;
import com.team3.entities.Candidate;
import com.team3.repositories.CandidateRepository;
import com.team3.services.CandidateService;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public List<CandidateDTO> searchCandidates(String keyword, String status) {
        List<Candidate> candidates;

        // If both keyword and status are provided
        if (keyword != null && !keyword.isEmpty() && status != null && !status.isEmpty()) {
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
        // If neither keyword nor status is provided, return all candidates
        else {
            candidates = candidateRepository.findAll();
        }

        // Convert the list of entities to DTOs
        return candidates.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CandidateDTO getCandidateById(Long id) {
        Optional<Candidate> candidate = candidateRepository.findById(id);
        // If candidate is found, map it to DTO; else return null
        return candidate.map(this::convertToDTO).orElse(null);
    }

    @Override
    public void saveCandidate(CandidateDTO candidateDTO) {
        Candidate candidate = convertToEntity(candidateDTO);
        candidateRepository.save(candidate);
    }

    @Override
    public void updateCandidate(CandidateDTO candidateDTO) {
        Candidate candidate = convertToEntity(candidateDTO);
        candidateRepository.save(candidate);
    }

    @Override
    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }

    // Convert Candidate entity to CandidateDTO
    private CandidateDTO convertToDTO(Candidate candidate) {
        return new CandidateDTO(
            candidate.getCandidateId(),
            candidate.getFullName(),
            candidate.getEmail(),
            candidate.getPhoneNumber(),
            candidate.getAddress(),
            candidate.getDateOfBirth(),
            candidate.getGender(),
            candidate.getCv(),
            candidate.getCurrentPosition(),
            candidate.getSkills(),
            candidate.getYearsOfExperience(),
            candidate.getHighestEducationLevel(),
            candidate.getStatus(),
            candidate.getNotes(),
            candidate.getCreatedAt(),
            candidate.getUpdatedAt()
        );
    }

    // Convert CandidateDTO to Candidate entity
    private Candidate convertToEntity(CandidateDTO candidateDTO) {
        return new Candidate(
            candidateDTO.getCandidateId(),
            candidateDTO.getFullName(),
            candidateDTO.getEmail(),
            candidateDTO.getPhoneNumber(),
            candidateDTO.getAddress(),
            candidateDTO.getDateOfBirth(),
            candidateDTO.getGender(),
            candidateDTO.getCv(),
            candidateDTO.getCurrentPosition(),
            candidateDTO.getSkills(),
            candidateDTO.getYearsOfExperience(),
            candidateDTO.getHighestEducationLevel(),
            null,  // recruiterOwner will be handled separately or injected
            candidateDTO.getStatus(),
            candidateDTO.getNotes(),
            candidateDTO.getCreatedAt(),
            candidateDTO.getUpdatedAt()
        );
    }

    
}
