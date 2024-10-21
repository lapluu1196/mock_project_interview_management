package com.team3.services.impl;

import com.team3.dtos.candidate.CandidateDTO;
import com.team3.dtos.interviewschedule.InterviewScheduleCreateDTO;
import com.team3.dtos.interviewschedule.InterviewScheduleDTO;
import com.team3.dtos.user.UserDTO;
import com.team3.entities.Candidate;
import com.team3.entities.InterviewSchedule;
import com.team3.entities.Job;
import com.team3.entities.User;
import com.team3.repositories.CandidateRepository;
import com.team3.repositories.InterviewScheduleRepository;
import com.team3.repositories.JobRepository;
import com.team3.repositories.UserRepository;
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

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class InterviewScheduleServiceImpl implements InterviewScheduleService {
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    public InterviewScheduleServiceImpl(InterviewScheduleRepository interviewScheduleRepository,
            CandidateRepository candidateRepository, JobRepository jobRepository, UserRepository userRepository) {
        this.interviewScheduleRepository = interviewScheduleRepository;
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
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

            if (keyword.matches("\\d{2}/\\d{2}/\\d{4}")) {
                // Nếu keyword chứa ngày (dd/MM/yyyy)
                LocalDate parsedDate = LocalDate.parse(keyword, dateFormatter);
                predicates.add(criteriaBuilder.equal(root.get("scheduleDate"), parsedDate));

            } else {
                // Nếu không phải ngày hoặc giờ thì tìm kiếm các trường khác
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("interviewTitle")),
                        "%" + keyword.trim().toLowerCase() + "%"));
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("status")),
                        "%" + keyword.trim().toLowerCase() + "%"));
                predicates.add(criteriaBuilder.equal(root.get("result"), keyword.trim()));
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
    public Page<InterviewScheduleDTO> findAll(String keyword, Long interviewerId, String statusName,
            Pageable pageable) {

        Specification<InterviewSchedule> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Nếu không có keyword, chỉ cần kiểm tra interviewerId và statusName
            if (keyword != null && !keyword.trim().isEmpty()) {
                String parseKeyword = keyword.trim().toLowerCase();

                // Kiểm tra tìm kiếm theo title, status, result, candidateName, jobTitle
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("interviewTitle")),
                        "%" + parseKeyword + "%"));
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("status")),
                        "%" + parseKeyword + "%"));
                predicates.add(criteriaBuilder.equal(root.get("result"), parseKeyword));
                predicates.add(criteriaBuilder.equal(root.get("candidate").get("fullName"), parseKeyword));
                predicates.add(criteriaBuilder.equal(root.get("job").get("jobTitle"), parseKeyword));
                Join<InterviewSchedule, User> interviewersJoin = root.join("interviewers", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(interviewersJoin.get("fullName")),
                        "%" + parseKeyword + "%"));
            }

            // Nếu keyword là một ngày (dd/MM/yyyy)
            if (keyword != null && keyword.matches("\\d{2}/\\d{2}/\\d{4}")) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate parsedDate = LocalDate.parse(keyword, dateFormatter);
                predicates.add(criteriaBuilder.equal(root.get("scheduleDate"), parsedDate));
            }

            // Nếu có chọn Interviewer
            if (interviewerId != null) {
                predicates.add(criteriaBuilder.equal(root.get("interviewers").get("userId"), interviewerId));
            }

            // Nếu có chọn Status
            if (statusName != null && !statusName.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), statusName));
            }

            // Nếu không có bất kỳ điều kiện nào thì trả về tất cả các bản ghi
            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction(); // Tạo một điều kiện luôn đúng (tìm tất cả)
            }

            // Sử dụng AND nếu có điều kiện tìm kiếm, nếu không chỉ trả về tất cả
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
    @Transactional
    public InterviewScheduleDTO create(InterviewScheduleCreateDTO interviewScheduleCreateDTO) {
        if (interviewScheduleCreateDTO == null) {
            throw new IllegalArgumentException("Interview schedule cannot be null");
        }

        // Kiểm tra nếu đã tồn tại lịch phỏng vấn với cùng tên
        var existingSchedule = interviewScheduleRepository
                .findByInterviewTitle(interviewScheduleCreateDTO.getInterviewTitle());
        if (existingSchedule != null) {
            throw new IllegalArgumentException("Interview schedule already exists");
        }

        // Tạo đối tượng InterviewSchedule mới
        var interviewSchedule = new InterviewSchedule();
        interviewSchedule.setInterviewTitle(interviewScheduleCreateDTO.getInterviewTitle());
        interviewSchedule.setScheduleDate(interviewScheduleCreateDTO.getScheduleDate());
        interviewSchedule.setScheduleFrom(interviewScheduleCreateDTO.getScheduleFrom());
        interviewSchedule.setScheduleTo(interviewScheduleCreateDTO.getScheduleTo());
        interviewSchedule.setLocation(interviewScheduleCreateDTO.getLocation());
        interviewSchedule.setRecruiterOwner(interviewScheduleCreateDTO.getRecruiterOwner());
        interviewSchedule.setMeetingId(interviewScheduleCreateDTO.getMeetingId());
        interviewSchedule.setNotes(interviewScheduleCreateDTO.getNotes());
        interviewSchedule.setStatus("Open");
        interviewSchedule.setResult("N/A");

        // Xử lý Candidate
        if (interviewScheduleCreateDTO.getCandidateId() != null) {
            Candidate candidate = new Candidate();
            candidate.setCandidateId(interviewScheduleCreateDTO.getCandidateId());
            interviewSchedule.setCandidate(candidate);
        }

        // Xử lý Job
        if (interviewScheduleCreateDTO.getJobId() != null) {
            Job job = new Job();
            job.setJobId(interviewScheduleCreateDTO.getJobId());
            interviewSchedule.setJob(job);
        }

        interviewScheduleRepository.save(interviewSchedule);

        interviewScheduleRepository.updateCandidateStatus(interviewSchedule.getCandidate().getCandidateId());
        // Xử lý danh sách interviewers (tạo các bản ghi trong bảng chung)
        if (interviewScheduleCreateDTO.getInterviewerIds() != null
                && !interviewScheduleCreateDTO.getInterviewerIds().isEmpty()) {
            for (Long userId : interviewScheduleCreateDTO.getInterviewerIds()) {
                User interviewer = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
                // Thêm từng cặp scheduleId và userId vào bảng chung
                interviewScheduleRepository.addInterviewerToSchedule(interviewSchedule.getScheduleId(),
                        interviewer.getUserId());
            }
        }



        // Convert entity sang DTO
        var interviewScheduleDTO = new InterviewScheduleDTO();
        interviewScheduleDTO.setScheduleId(interviewSchedule.getScheduleId());
        interviewScheduleDTO.setInterviewTitle(interviewSchedule.getInterviewTitle());
        interviewScheduleDTO.setScheduleDate(interviewSchedule.getScheduleDate());
        interviewScheduleDTO.setScheduleFrom(interviewSchedule.getScheduleFrom());
        interviewScheduleDTO.setScheduleTo(interviewSchedule.getScheduleTo());
        interviewScheduleDTO.setLocation(interviewSchedule.getLocation());
        interviewScheduleDTO.setRecruiterOwner(interviewSchedule.getRecruiterOwner());
        interviewScheduleDTO.setMeetingId(interviewSchedule.getMeetingId());
        interviewScheduleDTO.setNotes(interviewSchedule.getNotes());
        interviewScheduleDTO.setStatus(interviewSchedule.getStatus());
        interviewScheduleDTO.setResult(interviewSchedule.getResult());
        interviewScheduleDTO.setCreatedAt(interviewSchedule.getCreatedAt());
        interviewScheduleDTO.setUpdatedAt(interviewSchedule.getUpdatedAt());
        interviewScheduleDTO.setCandidateName(interviewSchedule.getCandidate().getFullName());
        interviewScheduleDTO.setJobTitle(interviewSchedule.getJob().getJobTitle());
        return interviewScheduleDTO;
    }

    @Override
    public List<InterviewSchedule> getAllSchedulesWithInterviewers() {
        return interviewScheduleRepository.findAll();
    }

    @Override
    public boolean isInterviewTitleExists(String interviewTitle) {
       return interviewScheduleRepository.existsByInterviewTitle(interviewTitle);
    }

    @Override
    public boolean existsByCandidateId(Long candidateId) {
        return interviewScheduleRepository.existsByCandidate_CandidateId(candidateId);
    }

}
