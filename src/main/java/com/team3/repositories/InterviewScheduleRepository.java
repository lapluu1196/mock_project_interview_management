package com.team3.repositories;

import com.team3.entities.Candidate;
import com.team3.entities.InterviewSchedule;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewScheduleRepository
        extends JpaRepository<InterviewSchedule, Long>, JpaSpecificationExecutor<InterviewSchedule> {
    InterviewSchedule findByInterviewTitle(String title);

    boolean existsByInterviewTitle(String interviewTitle);

    // Kiểm tra nếu ứng viên đã tồn tại trong bảng InterviewSchedule dựa vào
    // candidateId
    boolean existsByCandidate_CandidateId(Long candidateId);

    @Modifying
    @Query(value = "INSERT INTO interview_schedule_interviewers (schedule_id, user_id) VALUES (:scheduleId, :userId)", nativeQuery = true)
    void addInterviewerToSchedule(@Param("scheduleId") Long scheduleId, @Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM interview_schedule_interviewers WHERE schedule_id = :scheduleId AND user_id = :userId", nativeQuery = true)
    void deleteInterviewerFromSchedule(@Param("scheduleId") Long scheduleId, @Param("userId") Long userId);

    @Query(value = "SELECT user_id FROM interview_schedule_interviewers WHERE schedule_id = :scheduleId", nativeQuery = true)
    List<Long> findCurrentInterviewersByScheduleId(@Param("scheduleId") Long scheduleId);

    // Thêm query để cập nhật status của candidate
    @Modifying
    @Query(value = "UPDATE candidates SET status = :status WHERE candidate_id = :candidateId", nativeQuery = true)
    void updateCandidateStatus(@Param("candidateId") Long candidateId, String status);
    
    // Thêm query để cập nhật status của interview
    @Modifying
    @Query(value = "UPDATE interview_schedules SET status = :status WHERE schedule_id = :scheduleId", nativeQuery = true)
    void updateInterviewScheduleStatus(@Param("scheduleId") Long scheduleId, String status);



}
