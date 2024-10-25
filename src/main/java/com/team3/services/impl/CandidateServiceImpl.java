package com.team3.services.impl;

import com.team3.dtos.candidate.CandidateDTO;
import com.team3.entities.Candidate;
import com.team3.repositories.CandidateRepository;
import com.team3.services.CandidateService;
import com.team3.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<CandidateDTO> searchCandidates(String keyword, String status, Long userID, int page) {
        PageRequest pageable = PageRequest.of(page, 10);
        List<Candidate> candidates;

        String userRole = userService.getUserRole(userID);
        if ("Interviewer".equals(userRole)) {
            candidates = candidateRepository.findCandidatesForInterviewer(keyword, status, userID, pageable);
        } else {
            candidates = candidateRepository.findByKeywordAndStatus(keyword, status, pageable);
        }

        return candidates.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CandidateDTO getCandidateById(Long id) {
        return candidateRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    public void saveCandidate(CandidateDTO candidateDTO) {
        validateCandidateDTO(candidateDTO, true);
        Candidate candidate = convertToEntity(candidateDTO);
        candidate.setStatus("Open");
        candidateRepository.save(candidate);
    }

    @Override
    public void updateCandidate(CandidateDTO candidateDTO) {
        validateCandidateDTO(candidateDTO, false);
        Candidate candidate = convertToEntity(candidateDTO);
        candidateRepository.save(candidate);
    }

    @Override
    public void deleteCandidate(Long id) {
        Optional<Candidate> candidate = candidateRepository.findById(id);
        if (candidate.isPresent() && "Open".equals(candidate.get().getStatus())) {
            candidateRepository.deleteById(id);
        }
    }

    @Override
    public void banCandidate(Long id) {
        Optional<Candidate> candidate = candidateRepository.findById(id);
        if (candidate.isPresent()) {
            Candidate candidateEntity = candidate.get();
            candidateEntity.setStatus("Banned");
            candidateRepository.save(candidateEntity);
        }
    }

    // Implementation of getAllCandidates method
    @Override
    public List<CandidateDTO> getAllCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();
        return candidates.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private void validateCandidateDTO(CandidateDTO candidateDTO, boolean isNew) {
        if (candidateDTO.getFullName() == null || candidateDTO.getEmail() == null) {
            throw new IllegalArgumentException("ME002: Required field");
        }
        if (!candidateDTO.getEmail().matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
            throw new IllegalArgumentException("ME009: Invalid email address");
        }
        if (candidateDTO.getDateOfBirth() != null && candidateDTO.getDateOfBirth().isAfter(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("ME010: Date of Birth must be in the past");
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
