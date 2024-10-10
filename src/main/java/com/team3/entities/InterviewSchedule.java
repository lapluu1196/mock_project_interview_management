package com.team3.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "InterviewSchedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "interview_title", length = 150)
    private String interviewTitle;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(name = "schedule_date")
    private LocalDate scheduleDate;

    @Column(name = "schedule_from")
    private LocalTime scheduleFrom;

    @Column(name = "schedule_to")
    private LocalTime scheduleTo;

    @Column(name = "location", length = 255)
    private String location;

    @ManyToOne
    @JoinColumn(name = "interviewer_id", nullable = false)
    private User interviewer;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "result", length = 50)
    private String result;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
