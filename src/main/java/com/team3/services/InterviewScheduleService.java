package com.team3.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.team3.dtos.interviewschedule.InterviewScheduleDTO;

import com.team3.entities.InterviewSchedule;

public interface InterviewScheduleService {
    Page<InterviewScheduleDTO> findAll(String keyword, Pageable pageable);
    Page<InterviewScheduleDTO> findAll(Long userId , String status ,Pageable pageable);
    InterviewSchedule findById(Long id);
    InterviewSchedule save(InterviewSchedule interviewSchedule);
    List<InterviewSchedule> getAllSchedulesWithInterviewers();
}
