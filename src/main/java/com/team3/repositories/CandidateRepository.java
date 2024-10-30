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


    //Code dat: EM them de lam phan em ạ
    Candidate findFullNameByCandidateId(Long candidateId);

    // Minh
    // lấy ra danh sách ứng viên mà không bị banned
    @Query("SELECT c FROM Candidate c WHERE c.status != 'Banned'")
    List<Candidate> findAllNoBanned();
}
