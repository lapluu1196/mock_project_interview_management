package com.team3.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team3.entities.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

//    // Search candidates by keyword in name, email, or phone number and filter by status
//    List<Candidate> findByKeywordAndStatus(String keyword, String status);
//
//    // Find candidates by status only
//    List<Candidate> findByStatus(String status);
//
//    // Find candidates by keyword only
//    List<Candidate> findByKeyword(String keyword);
//
//    // If nothing is filtered, return all candidates
//    List<Candidate> findAll();
}