package com.team3.services;

import java.util.List;

import com.team3.dtos.candidate.CandidateDTO;


public interface CandidateService {

    // UC05: Search candidates by keyword, status, and role, with pagination
    List<CandidateDTO> searchCandidates(String keyword, String status, int page);

    // UC06: Save new candidate
    void saveCandidate(CandidateDTO candidateDTO);

    // UC07: Get candidate by ID
    CandidateDTO getCandidateById(Long id);

    // UC08: Update existing candidate
    void updateCandidate(CandidateDTO candidateDTO);

    // UC09: Delete candidate by ID
    void deleteCandidate(Long id);

    // UC10: Ban candidate
    void banCandidate(Long id);
    // Get all candidates

    //Dat: Anh dung xoa nhe

     List<CandidateDTO> getAllCandidates();
     

}
