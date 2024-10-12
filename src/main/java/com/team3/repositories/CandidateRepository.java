package com.team3.repositories;

import com.team3.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    
    // Find candidates by current position
    List<Candidate> findByCurrentPosition(String currentPosition);
    
    // Find candidates by status
    List<Candidate> findByStatus(String status);
}
