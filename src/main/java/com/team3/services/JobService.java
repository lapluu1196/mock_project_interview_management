package com.team3.services;

import java.util.List;
import java.util.Optional;

import com.team3.entities.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobService {
    List<Job> getAllJobs();
    Page<Job> searchJobs(String title, String status, Pageable pageable);

    void addJob(Job job);
    Optional<Job> findById(Long id);
    void save(Job job);
    void deleteJobById(Long jobId);
    List<Job> getAllJobsOpen();
}
