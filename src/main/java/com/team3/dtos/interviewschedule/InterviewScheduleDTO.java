package com.team3.dtos.interviewschedule;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.team3.dtos.user.UserDTO;
import com.team3.entities.Candidate;
import com.team3.entities.Job;
import com.team3.entities.User;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewScheduleDTO {
    
    private Long scheduleId;
    
    private String interviewTitle;

    private Candidate candidate;
    private Long candidateId;
    private String candidateName;
    
    private Long jobId;
    private Job job;
    private String jobTitle;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate scheduleDate;
    private LocalTime scheduleFrom;
    private LocalTime scheduleTo;

    // private String location;
    private List<UserDTO> interviewers;

    // private String recruiterOwner;
    // private String meetingId;
    // private String notes;

    private String status;
    private String result;
 
    // private LocalDateTime createdAt = LocalDateTime.now();
    // private LocalDateTime updatedAt = LocalDateTime.now();
}
