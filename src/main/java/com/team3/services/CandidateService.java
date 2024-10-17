package com.team3.services;

import java.util.List;

import com.team3.dtos.candidate.CandidateDTO;
import com.team3.entities.Candidate;

public interface CandidateService {

    // Search candidates by keyword and status
    // List<CandidateDTO> searchCandidates(String keyword, String status);

    // // Get candidate by ID
    // CandidateDTO getCandidateById(Long id);

    // // Save a new candidate
    // void saveCandidate(CandidateDTO candidateDTO);

    // // Update an existing candidate
    // void updateCandidate(CandidateDTO candidateDTO);

    // // Delete a candidate by ID
    // void deleteCandidate(Long id);

    // // Get all candidates
     List<CandidateDTO> getAllCandidates();
     

}
