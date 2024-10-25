package com.team3.services.impl;

import com.team3.entities.Job;
import com.team3.repositories.JobRepository;
import com.team3.services.JobService;

import java.util.List;

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
    
}
