package com.team3.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.team3.entities.Candidate;

@EnableJpaRepositories
public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {

    // UC05: Find candidates by keyword and status with pagination (for HR/Recruiter)
    @Query("SELECT c FROM Candidate c WHERE (c.fullName LIKE %:keyword% OR c.email LIKE %:keyword%) AND c.status = :status")
    List<Candidate> findByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") String status, Pageable pageable);

    // Find candidates by keyword (searching by name or email)
    @Query("SELECT c FROM Candidate c WHERE c.fullName LIKE %:keyword% OR c.email LIKE %:keyword%")
    List<Candidate> findByKeyword(@Param("keyword") String keyword);

    // Find candidates by status
    @Query("SELECT c FROM Candidate c WHERE c.status = :status")
    List<Candidate> findByStatus(@Param("status") String status);

    // Find candidate's full name by candidate ID
    @Query("SELECT c.fullName FROM Candidate c WHERE c.candidateId = :candidateId")
    Candidate findFullNameByCandidateId(@Param("candidateId") Long candidateId);

    //Code dat: EM them de lam phan em ạ
    //Candidate findFullNameByCandidateId(Long candidateId);

    // UC05: Find candidates assigned to a specific interviewer by keyword and status with pagination
    @Query("SELECT c FROM Candidate c JOIN c.interviewSchedules i WHERE (c.fullName LIKE %:keyword% OR c.email LIKE %:keyword%) AND c.status = :status AND i.interviewer.id = :interviewerId")
    List<Candidate> findCandidatesForInterviewer(
            @Param("keyword") String keyword, 
            @Param("status") String status, 
            @Param("interviewerId") Long interviewerId,
            Pageable pageable);

}