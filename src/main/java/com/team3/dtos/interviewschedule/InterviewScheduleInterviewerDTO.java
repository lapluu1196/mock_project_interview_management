package com.team3.dtos.interviewschedule;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class InterviewScheduleInterviewerDTO {
    private Long interviewerId;
    private String interviewName;
}
