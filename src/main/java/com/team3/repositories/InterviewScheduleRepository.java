package com.team3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team3.entities.InterviewSchedule;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long>, JpaSpecificationExecutor<InterviewSchedule> {
    InterviewSchedule findByInterviewTitle(String title);
    boolean existsByInterviewTitle(String interviewTitle);
    // Kiểm tra nếu ứng viên đã tồn tại trong bảng InterviewSchedule dựa vào candidateId
    boolean existsByCandidate_CandidateId(Long candidateId);
    @Modifying
    @Query(value = "INSERT INTO interview_schedule_interviewers (schedule_id, user_id) VALUES (:scheduleId, :userId)", nativeQuery = true)
    void addInterviewerToSchedule(@Param("scheduleId") Long scheduleId, @Param("userId") Long userId);


    // Thêm query để cập nhật status của candidate
    @Modifying
    @Query(value = "UPDATE candidates SET status = 'Waiting to interview' WHERE candidate_id = :candidateId", nativeQuery = true)
    void updateCandidateStatus(@Param("candidateId") Long candidateId);

}
