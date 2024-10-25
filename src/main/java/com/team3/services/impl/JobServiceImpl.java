package com.team3.services.impl;

import com.team3.entities.Job;
import com.team3.repositories.JobRepository;
import com.team3.services.JobService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    public Page<Job> searchJobs(String title, String status, Pageable pageable) {
        return jobRepository.searchJobs(title, status,pageable);
    }
    public void addJob(Job job) {
        jobRepository.save(job);
    }
    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }
    public void save(Job job) {
        jobRepository.save(job);
    }
    public void deleteJobById(Long jobId) {
        jobRepository.deleteById(jobId);
    }
}
