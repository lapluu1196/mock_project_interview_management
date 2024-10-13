package com.team3.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.team3.entities.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
