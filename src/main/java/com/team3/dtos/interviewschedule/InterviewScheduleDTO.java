package com.team3.dtos.interviewschedule;



import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.team3.dtos.candidate.CandidateDTO;
import com.team3.dtos.user.UserDTO;
import com.team3.entities.Candidate;
import com.team3.entities.Job;
import com.team3.entities.User;

import jakarta.validation.constraints.NotBlank;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewScheduleDTO {
    
    private Long scheduleId;
    
    @NotBlank(message = "Interview title is required!")
    private String interviewTitle;

    private CandidateDTO candidate;

    @NotBlank(message = "Candidate is required!")
    private Long candidateId;

    private String candidateName;
    
    @NotBlank(message = "Job is required!")
    private Long jobId;


    private Job job;
    private String jobTitle;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate scheduleDate;


    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime scheduleFrom;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime scheduleTo;

     private String location;

    @NotBlank(message = "Location is required!")
    private List<UserDTO> interviewers;
    
    private List<Long> interviewerIds;
    @NotBlank(message = "Recruiter is required!")
    private String recruiterOwner;
    private String meetingId;
    private String notes;

    private String status;
    private String result;
    
    @CreationTimestamp
    private LocalDateTime createdAt ;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt ;
}
