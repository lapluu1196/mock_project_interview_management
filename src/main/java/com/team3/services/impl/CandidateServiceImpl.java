package com.team3.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.team3.entities.User;
import com.team3.services.UserService;
import com.team3.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.team3.dtos.candidate.CandidateDTO;
import com.team3.entities.Candidate;
import com.team3.repositories.CandidateRepository;
import com.team3.services.CandidateService;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private UserService userService;

//    @Override
//    public List<CandidateDTO> searchCandidates(String keyword, String status, String role, int page) {
//        Pageable pageable = PageRequest.of(page, 10); // Pagination, 10 candidates per page
//        List<Candidate> candidates;
//
//
//        User currentUser = currentUserUtil.getCurrentUser();
//        String userRole = userService.getUserRole(currentUser.getUserId());
//
//        if ("Interviewer".equals(userRole)) {
//            candidates = candidateRepository.findCandidatesForInterviewer(keyword, status, currentUser.getInterviewSchedules(), pageable);
//        } else {
//            candidates = candidateRepository.findByKeywordAndStatus(keyword, status, pageable);
//        }
//
//        return candidates.stream().map(this::convertToDTO).collect(Collectors.toList());
//    }

    @Override
    public CandidateDTO getCandidateDTOById(Long id) {
        Optional<Candidate> candidate = candidateRepository.findById(id);
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


    //Dat: Code ham nay em can anh dung xoa nhe
//    @Override
//    public List<CandidateDTO> getAllCandidates() {
//        // Get List of entities
//        var candidates = candidateRepository.findAll();
//
//        // Convert entities to DTOs
//        var candidateDTOs = candidates.stream().map(candidate -> {
//            var candidateDTO = new CandidateDTO();
//
//            candidateDTO.setCandidateId(candidate.getCandidateId());
//            candidateDTO.setFullName(candidate.getFullName());
//            candidateDTO.setCv(candidate.getCv());
//            candidateDTO.setEmail(candidate.getEmail());
//            candidateDTO.setNotes(candidate.getNotes());
//            candidateDTO.setSkills(candidate.getSkills());
//            candidateDTO.setGender(candidate.getGender());
//            candidateDTO.setStatus(candidate.getStatus());
//            candidateDTO.setAddress(candidate.getAddress());
//            candidateDTO.setCurrentPosition(candidate.getCurrentPosition());
//            candidateDTO.setYearsOfExperience(candidate.getYearsOfExperience());
//            candidateDTO.setPhoneNumber(candidate.getPhoneNumber());
//            candidateDTO.setCreatedAt(candidate.getCreatedAt());
//            candidateDTO.setUpdatedAt(candidate.getUpdatedAt());
//
//            return candidateDTO;
//        }).toList();
//
//        // Return DTOs
//        return candidateDTOs;
//    }

    // Minh
    @Override
    public List<Candidate> getAllCandidatesNoBanned() {
        return candidateRepository.findAllNoBanned();
    }

    @Override
    public Candidate getCandidateById(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId).get();
        if (candidate == null) {
            throw new RuntimeException("Candidate not found");
        }
        return candidate;
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

