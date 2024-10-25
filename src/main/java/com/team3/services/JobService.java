package com.team3.services;

import java.util.List;

import com.team3.entities.Job;

public interface JobService {
    List<Job> getAllJobs();
    List<Job> getAllJobsOpen();
}
