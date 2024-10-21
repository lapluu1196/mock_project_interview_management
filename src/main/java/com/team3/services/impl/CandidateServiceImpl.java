package com.team3.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.team3.dtos.candidate.CandidateDTO;
import com.team3.entities.Candidate;
import com.team3.entities.User;
import com.team3.repositories.CandidateRepository;
import com.team3.services.CandidateService;
import com.team3.services.UserService;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<CandidateDTO> searchCandidates(String keyword, String status, String role, int page) {
        Pageable pageable = PageRequest.of(page, 10); // Pagination, 10 candidates per page
        List<Candidate> candidates;

        // Get the currently logged-in user
        User currentUser = userService.getCurrentUser();

        if (currentUser.getRole().equals("Interviewer")) {
            // Interviewer can only view candidates assigned to their interview schedules
            candidates = candidateRepository.findCandidatesForInterviewer(keyword, status, currentUser.getInterviewSchedules(), pageable);
        } else {
            // HR/Recruiter can view all candidates
            candidates = candidateRepository.findByKeywordAndStatus(keyword, status, pageable);
        }

        return candidates.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CandidateDTO getCandidateById(Long id) {
        Optional<Candidate> candidate = candidateRepository.findById(id);
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

    @Override
    public void banCandidate(Long id) {
        Optional<Candidate> candidateOpt = candidateRepository.findById(id);
        if (candidateOpt.isPresent()) {
            Candidate candidate = candidateOpt.get();
            candidate.setStatus("Banned");
            candidateRepository.save(candidate);
        }
    }

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
            null,
            candidateDTO.getStatus(),
            candidateDTO.getNotes(),
            candidateDTO.getCreatedAt(),
            candidateDTO.getUpdatedAt()
        );
    }
}
