package com.team3.services.impl;

import com.team3.entities.Job;
import com.team3.repositories.JobRepository;
import com.team3.services.JobService;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    @Override
    public List<Job> getAllJobsOpen() {
        return jobRepository.findJobByStatus("Open");
    }

    @Override
    public Page<Job> searchJobs(String title, String status, Pageable pageable) {
        return jobRepository.searchJobs(title, status,pageable);
    }
    @Override
    public void addJob(Job job) {
        jobRepository.save(job);
    }
    @Override
    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }
    @Override
    public void save(Job job) {
        jobRepository.save(job);
    }
    @Override
    public void deleteJobById(Long jobId) {
        jobRepository.deleteById(jobId);
    }
}
