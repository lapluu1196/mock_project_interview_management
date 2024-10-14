package com.team3.services.impl;

import com.team3.dtos.interviewschedule.InterviewScheduleDTO;
import com.team3.dtos.user.UserDTO;
import com.team3.entities.Candidate;
import com.team3.entities.InterviewSchedule;
import com.team3.entities.Job;
import com.team3.entities.User;
import com.team3.repositories.CandidateRepository;
import com.team3.repositories.InterviewScheduleRepository;
import com.team3.repositories.JobRepository;
import com.team3.services.InterviewScheduleService;

import java.time.LocalDate;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class InterviewScheduleServiceImpl implements InterviewScheduleService {
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;

    public InterviewScheduleServiceImpl(InterviewScheduleRepository interviewScheduleRepository,
            CandidateRepository candidateRepository, JobRepository jobRepository) {
        this.interviewScheduleRepository = interviewScheduleRepository;
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public Page<InterviewScheduleDTO> findAll(String keyword, Pageable pageable) {
        // Tạo Specification để tìm kiếm theo keyword
        Specification<InterviewSchedule> specification = (root, query, criteriaBuilder) -> {
            if (keyword == null) {
                return null;
            }

            List<Predicate> predicates = new ArrayList<>();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            if (keyword.matches("\\d{2}:\\d{2}")) {
                LocalTime parsedTime = LocalTime.parse(keyword, timeFormatter);

                // So sánh trực tiếp với trường TIME trong CSDL
                predicates.add(criteriaBuilder.equal(root.get("scheduleFrom"), parsedTime));
                predicates.add(criteriaBuilder.equal(root.get("scheduleTo"), parsedTime));

            } else if (keyword.matches("\\d{2}/\\d{2}/\\d{4}")) {
                // Nếu keyword chứa ngày (dd/MM/yyyy)
                LocalDate parsedDate = LocalDate.parse(keyword, dateFormatter);
                predicates.add(criteriaBuilder.equal(root.get("scheduleDate"), parsedDate));

            } else {
                // Nếu không phải ngày hoặc giờ thì tìm kiếm các trường khác
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("interviewTitle")),
                        "%" + keyword.toLowerCase() + "%"));
                predicates.add(criteriaBuilder.equal(root.get("status"), keyword));
                predicates.add(criteriaBuilder.equal(root.get("result"), keyword));
                predicates.add(criteriaBuilder.equal((root.get("candidate").get("fullName")), keyword));
                predicates.add(criteriaBuilder.equal((root.get("job").get("jobTitle")), keyword));
                predicates.add(criteriaBuilder.equal((root.get("interviewers").get("fullName")), keyword));
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };

        // Tìm danh sách InterviewSchedule dựa trên keyword
        var interviewSchedules = interviewScheduleRepository.findAll(specification, pageable);

        // Chuyển đổi Page<InterviewSchedule> sang Page<InterviewScheduleDTO>
        var interviewScheduleDTOs = interviewSchedules.map(interviewSchedule -> {
            var interviewScheduleDTO = new InterviewScheduleDTO();

            // Set các thuộc tính của InterviewScheduleDTO
            interviewScheduleDTO.setScheduleId(interviewSchedule.getScheduleId());
            interviewScheduleDTO.setInterviewTitle(interviewSchedule.getInterviewTitle());

            if (interviewSchedule.getResult() == null) {
                interviewScheduleDTO.setResult("N/A");
            } else {
                interviewScheduleDTO.setResult(interviewSchedule.getResult());
            }

            interviewScheduleDTO.setStatus(interviewSchedule.getStatus());
            interviewScheduleDTO.setScheduleDate(interviewSchedule.getScheduleDate());
            interviewScheduleDTO.setScheduleFrom(interviewSchedule.getScheduleFrom());
            interviewScheduleDTO.setScheduleTo(interviewSchedule.getScheduleTo());

            // Lấy thông tin candidate
            var candidate = candidateRepository
                    .findFullNameByCandidateId(interviewSchedule.getCandidate().getCandidateId());
            interviewScheduleDTO.setCandidateName(candidate.getFullName());

            // Lấy thông tin job
            var job = jobRepository.findJobTitleByJobId(interviewSchedule.getJob().getJobId());
            interviewScheduleDTO.setJobTitle(job.getJobTitle());

            // Lấy danh sách interviewers và chuyển sang UserDTO
            List<UserDTO> interviewers = interviewSchedule.getInterviewers().stream()
                    .map(interviewer -> {
                        UserDTO userDTO = new UserDTO();
                        userDTO.setUserId(interviewer.getUserId());
                        userDTO.setFullName(interviewer.getFullName());
                        return userDTO;
                    })
                    .collect(Collectors.toList());

            // Set danh sách interviewers vào DTO
            interviewScheduleDTO.setInterviewers(interviewers);

            return interviewScheduleDTO;
        });

        return interviewScheduleDTOs;
    }

    //
    @Override
    public Page<InterviewScheduleDTO> findAll(Long interviewerId, String status,Pageable pageable) {
        Specification<InterviewSchedule> specification = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("interviewers").get("userId"), interviewerId));

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };

        // Tìm danh sách InterviewSchedule dựa trên keyword
        var interviewSchedules = interviewScheduleRepository.findAll(specification, pageable);

        // Chuyển đổi Page<InterviewSchedule> sang Page<InterviewScheduleDTO>
        var interviewScheduleDTOs = interviewSchedules.map(interviewSchedule -> {
            var interviewScheduleDTO = new InterviewScheduleDTO();

            // Set các thuộc tính của InterviewScheduleDTO
            interviewScheduleDTO.setScheduleId(interviewSchedule.getScheduleId());
            interviewScheduleDTO.setInterviewTitle(interviewSchedule.getInterviewTitle());

            if (interviewSchedule.getResult() == null) {
                interviewScheduleDTO.setResult("N/A");
            } else {
                interviewScheduleDTO.setResult(interviewSchedule.getResult());
            }

            interviewScheduleDTO.setStatus(interviewSchedule.getStatus());
            interviewScheduleDTO.setScheduleDate(interviewSchedule.getScheduleDate());
            interviewScheduleDTO.setScheduleFrom(interviewSchedule.getScheduleFrom());
            interviewScheduleDTO.setScheduleTo(interviewSchedule.getScheduleTo());

            // Lấy thông tin candidate
            var candidate = candidateRepository
                    .findFullNameByCandidateId(interviewSchedule.getCandidate().getCandidateId());
            interviewScheduleDTO.setCandidateName(candidate.getFullName());

            // Lấy thông tin job
            var job = jobRepository.findJobTitleByJobId(interviewSchedule.getJob().getJobId());
            interviewScheduleDTO.setJobTitle(job.getJobTitle());

            // Lấy danh sách interviewers và chuyển sang UserDTO
            List<UserDTO> interviewers = interviewSchedule.getInterviewers().stream()
                    .map(interviewer -> {
                        UserDTO userDTO = new UserDTO();
                        userDTO.setUserId(interviewer.getUserId());
                        userDTO.setFullName(interviewer.getFullName());
                        return userDTO;
                    })
                    .collect(Collectors.toList());

            // Set danh sách interviewers vào DTO
            interviewScheduleDTO.setInterviewers(interviewers);

            return interviewScheduleDTO;
        });

        return interviewScheduleDTOs;
    }

    @Override
    public InterviewSchedule findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public InterviewSchedule save(InterviewSchedule interviewSchedule) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public List<InterviewSchedule> getAllSchedulesWithInterviewers() {
        return interviewScheduleRepository.findAll();
    }

}
