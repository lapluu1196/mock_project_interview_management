package com.team3.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team3.entities.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {

    // Find candidates by keyword (name or email) and status
    @Query("SELECT c FROM Candidate c WHERE (c.full_name LIKE %:keyword% OR c.email LIKE %:keyword%) AND c.status = :status")
    List<Candidate> findByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") String status);

    // Find candidates by keyword (searching by name or email)
    @Query("SELECT c FROM Candidate c WHERE c.full_name LIKE %:keyword% OR c.email LIKE %:keyword%")
    List<Candidate> findByKeyword(@Param("keyword") String keyword);

    // Find candidates by status
    @Query("SELECT c FROM Candidate c WHERE c.status = :status")
    List<Candidate> findByStatus(@Param("status") String status);

    // // Find candidate's full name by candidate ID
    // @Query("SELECT c.fullName FROM Candidate c WHERE c.id = :candidateId")
    // Candidate findFullNameByCandidateId(@Param("candidateId") Long candidateId);

    Candidate findFullNameByCandidateId(Long candidateId);
}
