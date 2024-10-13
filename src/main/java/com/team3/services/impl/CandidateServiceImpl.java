package com.team3.services.impl;

import com.team3.repositories.CandidateRepository;
import com.team3.services.CandidateService;
import org.springframework.stereotype.Service;

@Service
public class CandidateServiceImpl implements CandidateService {
    private final CandidateRepository candidateRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }
}
