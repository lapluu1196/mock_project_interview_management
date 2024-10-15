package com.team3.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.team3.entities.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {

    // Find candidates by keyword and status
    List<Candidate> findByKeywordAndStatus(String keyword, String status);

    // Find candidates by keyword only
    List<Candidate> findByKeyword(String keyword);

    // Find candidates by status only
    List<Candidate> findByStatus(String status);

    Candidate findFullNameByCandidateId(Long candidateId);
}
