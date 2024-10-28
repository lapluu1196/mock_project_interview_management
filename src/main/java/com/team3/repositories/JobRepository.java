package com.team3.repositories;

import com.team3.entities.Job;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    Job findJobTitleByJobId(Long jobId);
    List <Job> findJobByStatus(String status);
    @Query("SELECT j FROM Job j WHERE " +
            "(:title IS NULL OR LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:status IS NULL OR j.status = :status)")
    Page<Job> searchJobs(@Param("title") String title, @Param("status") String status, Pageable pageable);
    Optional<Job> findById(Long id);
}
