package com.team3.services;

import java.util.List;

import com.team3.dtos.candidate.CandidateDTO;
import com.team3.entities.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CandidateService {

     Page<CandidateDTO> filterCandidate(String search, String status, Pageable pageable);

    // //Dat: Anh dung xoa nhe

     List<CandidateDTO> getAllCandidates();

     // Minh
     public List<Candidate> getAllCandidatesNoBanned();

     Candidate getCandidateById(Long candidateId);

}
