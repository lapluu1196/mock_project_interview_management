package com.team3.dtos.interviewschedule;



import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.team3.dtos.candidate.CandidateDTO;
import com.team3.dtos.user.UserDTO;
import com.team3.entities.Candidate;
import com.team3.entities.Job;
import com.team3.entities.User;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewScheduleCreateDTO {
    
    private Long scheduleId;
    
    @NotBlank(message = "Interview title is required!")
    private String interviewTitle;

    private CandidateDTO candidate;

    @NotNull(message = "Candidate is required!")
    private Long candidateId;

    private String candidateName;
    
    @NotNull(message = "Job is required!")
    private Long jobId;


    private Job job;
    private String jobTitle;


    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "Schedule date is required!")
    private LocalDate scheduleDate;


    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime scheduleFrom;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime scheduleTo;

    // @NotBlank(message = "Location is required!")
    private String location;

    
    private List<UserDTO> interviewers;

    @NotEmpty(message = "Interviewer is required!")
    @Size(min = 1, message = "At least one Interviewer is required!")
    private List< @NotNull(message = "Interviewer is required!") Long > interviewerIds; 

    @NotBlank(message = "Recruiter is required!")
    private String recruiterOwner;


    private String meetingId;

    @Length(max = 500, message = "Notes must be less than 500 characters")
    private String notes;
    
    @CreationTimestamp
    private LocalDateTime createdAt ;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt ;
    
}
