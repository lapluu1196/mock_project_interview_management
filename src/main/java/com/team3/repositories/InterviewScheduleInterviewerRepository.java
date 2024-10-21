package com.team3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.team3.entities.InterviewSchedule;

public interface InterviewScheduleInterviewerRepository extends JpaRepository<InterviewSchedule, Long>, JpaSpecificationExecutor<InterviewSchedule> {

}
