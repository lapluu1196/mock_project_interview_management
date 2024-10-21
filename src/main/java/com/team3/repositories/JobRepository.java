package com.team3.repositories;

import com.team3.entities.Job;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    Job findJobTitleByJobId(Long jobId);
    List <Job> findJobByStatus(String status);

}
