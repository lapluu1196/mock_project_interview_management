package com.team3.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.team3.dtos.interviewschedule.InterviewScheduleCreateDTO;
import com.team3.dtos.interviewschedule.InterviewScheduleDTO;
import com.team3.entities.InterviewSchedule;

public interface InterviewScheduleService {
    Page<InterviewScheduleDTO> findAll(String keyword, Pageable pageable);
    Page<InterviewScheduleDTO> findAll(String keyword, Long userId , String status ,Pageable pageable);
    InterviewSchedule findById(Long id);
    InterviewScheduleDTO create(InterviewScheduleCreateDTO interviewScheduleCreateDTO);
    List<InterviewSchedule> getAllSchedulesWithInterviewers();

    boolean isInterviewTitleExists(String interviewTitle);
    boolean existsByCandidateId(Long candidateId);
}
