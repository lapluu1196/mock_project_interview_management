package com.team3.services.impl;


import com.team3.dtos.interviewschedule.InterviewScheduleCreateDTO;
import com.team3.dtos.interviewschedule.InterviewScheduleDTO;
import com.team3.dtos.log.LogDTO;
import com.team3.dtos.user.UserDTO;
import com.team3.entities.Candidate;
import com.team3.entities.InterviewSchedule;
import com.team3.entities.Job;
import com.team3.entities.Log;
import com.team3.entities.User;
import com.team3.repositories.CandidateRepository;
import com.team3.repositories.InterviewScheduleRepository;
import com.team3.repositories.JobRepository;
import com.team3.repositories.LogRepository;
import com.team3.repositories.UserRepository;
import com.team3.services.InterviewScheduleService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class InterviewScheduleServiceImpl implements InterviewScheduleService {

    @Autowired
    private InterviewScheduleRepository interviewScheduleRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;

    // Find by id
    @Override
    public InterviewScheduleDTO findById(Long id) {
        InterviewSchedule interviewSchedule = interviewScheduleRepository.findById(id).orElse(null);

        if (interviewSchedule == null) {
            throw new IllegalArgumentException("Schedule not found!");
        }

        InterviewScheduleDTO interviewScheduleDTO = new InterviewScheduleDTO();

        interviewScheduleDTO.setScheduleId(interviewSchedule.getScheduleId());
        interviewScheduleDTO.setInterviewTitle(interviewSchedule.getInterviewTitle());

        interviewScheduleDTO.setStatus(interviewSchedule.getStatus());
        interviewScheduleDTO.setScheduleDate(interviewSchedule.getScheduleDate());
        interviewScheduleDTO.setScheduleFrom(interviewSchedule.getScheduleFrom());
        interviewScheduleDTO.setScheduleTo(interviewSchedule.getScheduleTo());
        interviewScheduleDTO.setMeetingId(interviewSchedule.getMeetingId());
        interviewScheduleDTO.setResult(interviewSchedule.getResult());
        interviewScheduleDTO.setNotes(interviewSchedule.getNotes());
        interviewScheduleDTO.setLocation(interviewSchedule.getLocation());
        interviewScheduleDTO.setRecruiterOwner(interviewSchedule.getRecruiterOwner());
        interviewScheduleDTO.setCandidateId(interviewSchedule.getCandidate().getCandidateId());
        interviewScheduleDTO.setJobId(interviewSchedule.getJob().getJobId());
        interviewScheduleDTO.setCreatedAt(interviewSchedule.getCreatedAt());
        interviewScheduleDTO.setUpdatedAt(interviewSchedule.getUpdatedAt());

        interviewScheduleDTO.setRecruiterOwner(interviewSchedule.getRecruiterOwner());
        var candidate = candidateRepository
                .findFullNameByCandidateId(interviewSchedule.getCandidate().getCandidateId());
        interviewScheduleDTO.setCandidateName(candidate.getFullName());

        var job = jobRepository.findJobTitleByJobId(interviewSchedule.getJob().getJobId());
        interviewScheduleDTO.setJobTitle(job.getJobTitle());

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

        // Lấy danh sách interviewerIds từ Interviewers của InterviewSchedule
        if (interviewSchedule.getInterviewers() != null) {
            List<Long> interviewerIds = interviewSchedule.getInterviewers().stream()
                    .map(User::getUserId)
                    .collect(Collectors.toList());
            interviewScheduleDTO.setInterviewerIds(interviewerIds);
        }

        return interviewScheduleDTO;
    }

    // Ham create
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

        interviewSchedule.setRecruiterOwner(interviewScheduleCreateDTO.getRecruiterOwner());
        interviewSchedule.setLocation(interviewScheduleCreateDTO.getLocation());
        interviewSchedule.setNotes(interviewScheduleCreateDTO.getNotes());
        interviewSchedule.setMeetingId(interviewScheduleCreateDTO.getMeetingId());
        interviewSchedule.setStatus("New");

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
        logUserAction("Created interview schedule with id: " + interviewSchedule.getScheduleId(), "Create");


        interviewScheduleRepository.updateCandidateStatus(interviewSchedule.getCandidate().getCandidateId(),
                "Waiting to interview");
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

    // Update
    @Override
    @Transactional
    public InterviewScheduleDTO update(Long id, InterviewScheduleDTO interviewScheduleDTO) {
        var interviewSchedule = interviewScheduleRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interview schedule not found"));

        interviewSchedule.setInterviewTitle(interviewScheduleDTO.getInterviewTitle());
        interviewSchedule.setScheduleDate(interviewScheduleDTO.getScheduleDate());
        interviewSchedule.setScheduleFrom(interviewScheduleDTO.getScheduleFrom());
        interviewSchedule.setScheduleTo(interviewScheduleDTO.getScheduleTo());

        interviewSchedule.setRecruiterOwner(interviewScheduleDTO.getRecruiterOwner());
        interviewSchedule.setLocation(interviewScheduleDTO.getLocation());
        interviewSchedule.setNotes(interviewScheduleDTO.getNotes());
        interviewSchedule.setMeetingId(interviewScheduleDTO.getMeetingId());
        if (interviewScheduleDTO.getResult() != null && !interviewScheduleDTO.getResult().isEmpty()) {
            interviewSchedule.setStatus("Interviewed");
        } else {
            interviewSchedule.setStatus("");
        }
        interviewSchedule.setResult(interviewScheduleDTO.getResult());

        // Xử lý candidate: kiểm tra nếu admin chọn candidate khác candidate hiện tại
        if (interviewScheduleDTO.getCandidateId() != null) {
            Candidate currentCandidate = interviewSchedule.getCandidate();
            Long newCandidateId = interviewScheduleDTO.getCandidateId();

            // Nếu candidate mới khác candidate hiện tại, update status
            if (currentCandidate != null && !currentCandidate.getCandidateId().equals(newCandidateId)) {
                // Cập nhật status của candidate cũ thành null
                interviewScheduleRepository.updateCandidateStatus(currentCandidate.getCandidateId(),
                        "Waiting to interview");

                // Cập nhật thông tin candidate mới
                Candidate newCandidate = new Candidate();
                newCandidate.setCandidateId(newCandidateId);
                interviewSchedule.setCandidate(newCandidate);

                // Cập nhật status của candidate mới
                if (interviewSchedule.getResult().equals("Passed")) {
                    interviewScheduleRepository.updateCandidateStatus(newCandidateId, "Passed interview");
                }
                if (interviewSchedule.getResult().equals("Failed")) {
                    interviewScheduleRepository.updateCandidateStatus(newCandidateId, "Failed interview");
                } else {
                    interviewScheduleRepository.updateCandidateStatus(newCandidateId, "Waiting to interview");
                }
            } else {
                // Cập nhật status của candidate mới
                if (interviewSchedule.getResult().equals("Passed")) {
                    interviewScheduleRepository.updateCandidateStatus(currentCandidate.getCandidateId(),
                            "Passed interview");
                }
                if (interviewSchedule.getResult().equals("Failed")) {
                    interviewScheduleRepository.updateCandidateStatus(currentCandidate.getCandidateId(),
                            "Failed interview");
                }
            }
        }

        // Xử lý Job nếu có
        if (interviewScheduleDTO.getJobId() != null) {
            Job job = new Job();
            job.setJobId(interviewScheduleDTO.getJobId());
            interviewSchedule.setJob(job);
        }

        interviewScheduleRepository.save(interviewSchedule);
        logUserAction("Update interview schedule with id: " + interviewSchedule.getScheduleId(), "Update");


        // Xử lý danh sách interviewers
        if (interviewScheduleDTO.getInterviewerIds() != null && !interviewScheduleDTO.getInterviewerIds().isEmpty()) {
            // Lấy danh sách interviewers hiện tại của schedule
            List<Long> currentInterviewers = interviewScheduleRepository
                    .findCurrentInterviewersByScheduleId(interviewSchedule.getScheduleId());

            // Lọc ra các interviewers cần xóa (có trong danh sách hiện tại nhưng không có
            // trong danh sách mới)
            for (Long currentUserId : currentInterviewers) {
                if (!interviewScheduleDTO.getInterviewerIds().contains(currentUserId)) {
                    interviewScheduleRepository.deleteInterviewerFromSchedule(interviewSchedule.getScheduleId(),
                            currentUserId);
                }
            }
            List<Long> newInterviewerIds = interviewScheduleDTO.getInterviewerIds();
            // Lọc ra các interviewers cần thêm (có trong danh sách mới nhưng không có trong
            // danh sách hiện tại)
            for (Long newUserId : newInterviewerIds) {

                if(newUserId == null){
                    continue;
                }
                else{
                    if (!currentInterviewers.contains(newUserId)) {
                        User interviewer = userRepository.findById(newUserId)
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));
                        interviewScheduleRepository.addInterviewerToSchedule(interviewSchedule.getScheduleId(),
                                interviewer.getUserId());
                    }
                }
            }
        }

        // Convert entity sang DTO
        var updatedInterviewScheduleDTO = new InterviewScheduleDTO();
        updatedInterviewScheduleDTO.setScheduleId(interviewSchedule.getScheduleId());
        updatedInterviewScheduleDTO.setInterviewTitle(interviewSchedule.getInterviewTitle());
        updatedInterviewScheduleDTO.setScheduleDate(interviewSchedule.getScheduleDate());
        updatedInterviewScheduleDTO.setScheduleFrom(interviewSchedule.getScheduleFrom());
        updatedInterviewScheduleDTO.setScheduleTo(interviewSchedule.getScheduleTo());
        updatedInterviewScheduleDTO.setLocation(interviewSchedule.getLocation());
        updatedInterviewScheduleDTO.setRecruiterOwner(interviewSchedule.getRecruiterOwner());
        updatedInterviewScheduleDTO.setMeetingId(interviewSchedule.getMeetingId());
        updatedInterviewScheduleDTO.setNotes(interviewSchedule.getNotes());
        updatedInterviewScheduleDTO.setStatus(interviewSchedule.getStatus());
        updatedInterviewScheduleDTO.setResult(interviewSchedule.getResult());
        updatedInterviewScheduleDTO.setCreatedAt(interviewSchedule.getCreatedAt());
        updatedInterviewScheduleDTO.setUpdatedAt(interviewSchedule.getUpdatedAt());
        updatedInterviewScheduleDTO.setCandidateName(interviewSchedule.getCandidate().getFullName());
        updatedInterviewScheduleDTO.setJobTitle(interviewSchedule.getJob().getJobTitle());

        return updatedInterviewScheduleDTO;
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

    //
    @Override
    public Page<InterviewScheduleDTO> filterSchedule(String keyword, Long interviewerId, String status,
            Pageable pageable) {
        Specification<InterviewSchedule> spec = (Specification<InterviewSchedule>) (root, query, criteriaBuilder) -> {
            // Trường hợp không có giá trị tìm kiếm nào (keyword, status, interviewerId đều
            // null hoặc rỗng)
            if ((keyword == null || keyword.isEmpty()) && (status == null || status.isEmpty())
                    && (interviewerId == null)) {
                return null;
            }

            // Xây dựng danh sách các Predicate để giữ các điều kiện
            List<Predicate> predicates = new ArrayList<>();
            // Điều kiện tìm kiếm theo từ khóa (keyword)
            if (keyword != null && !keyword.isEmpty()) {
                Join<InterviewSchedule, User> interviewersJoin = root.join("interviewers", JoinType.LEFT);
                // Tạo một danh sách để chứa các điều kiện tìm kiếm
                List<Predicate> keywordPredicates = new ArrayList<>();

                // Điều kiện tìm kiếm với các cột chuỗi
                keywordPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("interviewTitle")),
                        "%" + keyword.trim().toLowerCase() + "%"));
                keywordPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("status")),
                        "%" + keyword.trim().toLowerCase() + "%"));
                keywordPredicates.add(criteriaBuilder.equal(root.get("result"), keyword));
                keywordPredicates.add(criteriaBuilder.equal(root.get("candidate").get("fullName"), keyword));
                keywordPredicates.add(criteriaBuilder.equal(root.get("job").get("jobTitle"), keyword));
                keywordPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(interviewersJoin.get("fullName")),
                        "%" + keyword.trim().toLowerCase() + "%"));

                // // Kiểm tra nếu từ khóa là ngày hợp lệ (dd/MM/yyyy)
                try {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate date = LocalDate.parse(keyword.trim(), dateFormatter);
                    keywordPredicates.add(
                            criteriaBuilder.equal(root.get("scheduleDate"), date));
                } catch (DateTimeParseException e) {
                    // Bỏ qua nếu không phải ngày hợp lệ
                }

                // Kết hợp các điều kiện lại với nhau bằng OR
                predicates.add(criteriaBuilder.or(keywordPredicates.toArray(new Predicate[0])));
            }

            // Điều kiện lọc theo trạng thái (status) từ combo box
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // Điều kiện lọc theo interviewerId từ combo box
            if (interviewerId != null) {
                // Giả định bảng `User` có một quan hệ (join) với bảng `Interviewers`
                predicates.add(criteriaBuilder.equal(root.get("interviewers").get("userId"), interviewerId));
            }

            // Trả về điều kiện kết hợp (AND) của tất cả các Predicate
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        var interviewschedules = interviewScheduleRepository.findAll(spec, pageable);

        return interviewschedules.map(interviewerToSchedule -> {
            InterviewScheduleDTO interviewScheduleDTO = new InterviewScheduleDTO();
            interviewScheduleDTO.setScheduleId(interviewerToSchedule.getScheduleId());
            interviewScheduleDTO.setInterviewTitle(interviewerToSchedule.getInterviewTitle());
            interviewScheduleDTO.setScheduleDate(interviewerToSchedule.getScheduleDate());
            interviewScheduleDTO.setScheduleFrom(interviewerToSchedule.getScheduleFrom());
            interviewScheduleDTO.setScheduleTo(interviewerToSchedule.getScheduleTo());
            interviewScheduleDTO.setLocation(interviewerToSchedule.getLocation());
            interviewScheduleDTO.setRecruiterOwner(interviewerToSchedule.getRecruiterOwner());
            interviewScheduleDTO.setMeetingId(interviewerToSchedule.getMeetingId());
            interviewScheduleDTO.setNotes(interviewerToSchedule.getNotes());
            interviewScheduleDTO.setStatus(interviewerToSchedule.getStatus());
            if(interviewerToSchedule.getResult() != null){
                interviewScheduleDTO.setResult(interviewerToSchedule.getResult());
            }
            else{
                interviewScheduleDTO.setResult("N/A");
            }
            interviewScheduleDTO.setCreatedAt(interviewerToSchedule.getCreatedAt());
            interviewScheduleDTO.setUpdatedAt(interviewerToSchedule.getUpdatedAt());
            interviewScheduleDTO.setCandidateName(interviewerToSchedule.getCandidate().getFullName());
            interviewScheduleDTO.setJobTitle(interviewerToSchedule.getJob().getJobTitle());
            // Lấy danh sách interviewers và chuyển sang UserDTO
            List<UserDTO> interviewers = interviewerToSchedule.getInterviewers().stream()
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
    }

    @Transactional
    public boolean cancelStatusById(Long id) {
        if (interviewScheduleRepository.existsById(id)) {
            interviewScheduleRepository.updateInterviewScheduleStatus(id, "Canceled");
            logUserAction("Cancelled interview schedule with id: " + id, "Cancel");
            return true;
        }
        return false;

    }

    // Lấy id người dùng
    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
        // Check if the principal is an instance of Spring Security's User
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
            return userRepository.findByUsername(username).getUserId();
        }
        
        // Alternatively, if the principal is already your custom User entity
        if (principal instanceof com.team3.entities.User) {
            return ((com.team3.entities.User) principal).getUserId();
        }
    
        // If no valid user found, throw an exception or handle as needed
        throw new IllegalStateException("User not authenticated or unknown principal type");
    }
    

    public void logUserAction(String actionDescription, String action) {
        Long userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElse(null);
        if (userId != null) {
            // Tạo log và lưu vào cơ sở dữ liệu
            Log log = new Log();
            log.setUser(user);
            log.setAction(action);
            log.setEntityType("InterviewSchedule");
            log.setDescription(actionDescription);
            log.setTimestamp(LocalDateTime.now());

            // Lưu log vào database
            logRepository.save(log);
        }
    }

}
