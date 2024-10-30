package com.team3.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.team3.entities.User;
import com.team3.services.UserService;
import com.team3.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public Page<CandidateDTO> filterCandidate(String search, String status, Pageable pageable) {

        Specification<Candidate> spec = (Specification<Candidate>) (root, query, criteriaBuilder) -> {
            if ((search == null || search.isEmpty()) && (status == null || status.isEmpty())) {
                return null;
            }

            if (search == null || search.isEmpty()) {
                return criteriaBuilder.equal(root.get("status"), status);
            }

            if (status == null || status.isEmpty()) {
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("fullName"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("email"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("phoneNumber"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("currentPosition"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("status"), "%" + search + "%")
                ) ;
            }

            return criteriaBuilder.and(
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("fullName"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("email"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("phoneNumber"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("currentPosition"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("status"), "%" + search + "%")
                    ),
                    criteriaBuilder.equal(root.get("status"), status)
            );
        };

        var candidates = candidateRepository.findAll(spec, pageable);

        return candidates.map(this::convertToDTO);
    }

    // Implementation of getAllCandidates method
    @Override
    public List<CandidateDTO> getAllCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();
        return candidates.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

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
                candidate.getRecruiterOwner().getUsername(),
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

