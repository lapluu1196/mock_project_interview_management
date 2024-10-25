package com.team3.controllers.interviewschedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team3.dtos.candidate.CandidateDTO;
import com.team3.dtos.interviewschedule.InterviewScheduleCreateDTO;
import com.team3.dtos.interviewschedule.InterviewScheduleDTO;
import com.team3.dtos.interviewschedule.TimeUtils;
import com.team3.dtos.user.UserDTO;
import com.team3.entities.Candidate;
import com.team3.entities.InterviewSchedule;
import com.team3.entities.Job;
import com.team3.entities.User;
import com.team3.services.CandidateService;
import com.team3.services.InterviewScheduleService;
import com.team3.services.JobService;
import com.team3.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/interview-schedule")
public class InterviewScheduleController {

    @Autowired
    private InterviewScheduleService interviewScheduleService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

    @GetMapping("/index")
    public String index(@RequestParam(required = false) String keyword,
            @RequestParam(name = "interviewerId", required = false) Long interviewerId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        int size = 10;
        ;
        var pageable = PageRequest.of(page, size);
        Page<InterviewScheduleDTO> interviewScheduleDTOs = interviewScheduleService.filterSchedule(keyword, interviewerId,
                status, pageable);

        List<UserDTO> interviewers = userService.getInterviewers();
        List<InterviewSchedule> schedules = interviewScheduleService.getAllSchedulesWithInterviewers();
        if (interviewScheduleDTOs.isEmpty()) {
            model.addAttribute("message", "Data not found!");
        }
        model.addAttribute("schedules", schedules);
        model.addAttribute("interviewers", interviewers);
        model.addAttribute("interviewScheduleDTOs", interviewScheduleDTOs);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", interviewScheduleDTOs.getTotalPages());
        model.addAttribute("totalUsers", interviewScheduleDTOs.getTotalElements());
        return "contents/interviewschedule/schedule_list";
    }

    @GetMapping("/add")
    public String addInterviewSchedule(HttpServletRequest request,Model model) {
        var interviewCreateDTO = new InterviewScheduleCreateDTO();
        model.addAttribute("interviewScheduleCreateDTO", interviewCreateDTO);
        List<UserDTO> interviewers = userService.getInterviewers();
        model.addAttribute("interviewers", interviewers);
        var candidates = candidateService.getAllCandidates();
        model.addAttribute("candidates", candidates);

        var jobs = jobService.getAllJobsOpen();
        model.addAttribute("jobs", jobs);

        var recruiters = userService.getRecruiters();
        model.addAttribute("recruiters", recruiters);
        // Lấy URL của trang trước (nếu có)
        String previousPageUrl = request.getHeader("Referer");
        if (previousPageUrl == null || previousPageUrl.isEmpty()) {
            previousPageUrl = "/interview-schedule/index"; // URL mặc định nếu không có Referer
        }
        model.addAttribute("previousPageUrl", previousPageUrl);
        return "contents/interviewschedule/schedule_create";
    }

    @PostMapping("/add")
    public String addInterviewSchedule(
            @ModelAttribute @Valid InterviewScheduleCreateDTO interviewScheduleCreateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Kiểm tra nếu interviewTitle đã tồn tại
        if (interviewScheduleService.isInterviewTitleExists(interviewScheduleCreateDTO.getInterviewTitle())) {

            bindingResult.rejectValue("interviewTitle", "interviewTitle.exists", "Interview title already exists!");

        }

        if (interviewScheduleCreateDTO.getScheduleFrom().isAfter(interviewScheduleCreateDTO.getScheduleTo())) {

            bindingResult.rejectValue("scheduleFrom", "scheduleFrom.invalid",
                    "Schedule to must be after schedule from!");

        }

        if (interviewScheduleService.existsByCandidateId(interviewScheduleCreateDTO.getCandidateId())) {

            bindingResult.rejectValue("candidateId", "candidateId.exists", "Candidate already scheduled!");
        }

        // Kiểm tra BindingResult có lỗi không
        if (bindingResult.hasErrors()) {
            // Nếu có lỗi, giữ lại các lựa chọn cho các trường

            List<UserDTO> interviewers = userService.getInterviewers();
            model.addAttribute("interviewers", interviewers);

            List<CandidateDTO> candidates = candidateService.getAllCandidates();
            model.addAttribute("candidates", candidates);

            List<Job> jobs = jobService.getAllJobsOpen();
            model.addAttribute("jobs", jobs);

            List<UserDTO> recruiters = userService.getRecruiters();
            model.addAttribute("recruiters", recruiters);

            return "contents/interviewschedule/schedule_create"; // Trả lại form nếu có lỗi
        }

        // Tiến hành tạo lịch phỏng vấn nếu không có lỗi
        var result = interviewScheduleService.create(interviewScheduleCreateDTO);
        if (result == null) {
            model.addAttribute("errorPopupMessage", "Failed to created interview schedule");

            // Nếu không tạo thành công, ném lỗi
            throw new IllegalArgumentException("Failed to create schedule");
        }

        // Thêm thông báo thành công vào RedirectAttributes
        redirectAttributes.addFlashAttribute("successMessage", "Successfully created schedule!");

        // Redirect về trang quản lý sau khi thêm thành công
        return "redirect:/interview-schedule/index";
    }

    @GetMapping("/scheduleDetail/{id}")
    public String scheduleDetail(@PathVariable("id") Long id, Model model) {

        InterviewScheduleDTO interviewSchedule = interviewScheduleService.findById(id);
        String formattedFrom = TimeUtils.formatTimeTo12Hour(interviewSchedule.getScheduleFrom());
        String formattedTo = TimeUtils.formatTimeTo12Hour(interviewSchedule.getScheduleTo());

        model.addAttribute("scheduleFrom", formattedFrom);
        model.addAttribute("scheduleTo", formattedTo);
        model.addAttribute("interviewSchedule", interviewSchedule);
        return "contents/interviewschedule/schedule_detail";
    }

    //Edit interview schedule
    @GetMapping("/edit/{id}")
    public String updateInterviewSchedule(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        // Tìm kiếm thông tin phỏng vấn
        InterviewScheduleDTO interviewScheduleDTO = interviewScheduleService.findById(id);
        model.addAttribute("interviewScheduleDTO", interviewScheduleDTO);

        // Định dạng thời gian
        String formattedFrom = TimeUtils.formatTimeTo12Hour(interviewScheduleDTO.getScheduleFrom());
        String formattedTo = TimeUtils.formatTimeTo12Hour(interviewScheduleDTO.getScheduleTo());
        model.addAttribute("scheduleFrom", formattedFrom);
        model.addAttribute("scheduleTo", formattedTo);

        // Lấy các danh sách cần thiết (người phỏng vấn, ứng viên, công việc, nhà tuyển
        // dụng)
        List<UserDTO> interviewers = userService.getInterviewers();
        List<CandidateDTO> candidates = candidateService.getAllCandidates();
        List<Job> jobs = jobService.getAllJobsOpen();
        List<UserDTO> recruiters = userService.getRecruiters();

        model.addAttribute("interviewers", interviewers);
        model.addAttribute("candidates", candidates);
        model.addAttribute("jobs", jobs);
        model.addAttribute("recruiters", recruiters);

        // Lấy URL của trang trước (nếu có)
        String previousPageUrl = request.getHeader("Referer");
        if (previousPageUrl == null || previousPageUrl.isEmpty()) {
            previousPageUrl = "/interview-schedule/index"; // URL mặc định nếu không có Referer
        }
        model.addAttribute("previousPageUrl", previousPageUrl);

        return "contents/interviewschedule/schedule_edit";
    }

    @PostMapping("/edit/{id}")
    public String updateInterviewSchedulePost(@PathVariable("id") Long id,
            @Valid InterviewScheduleDTO interviewScheduleDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        InterviewScheduleDTO existingSchedule = interviewScheduleService.findById(id);

        // Kiểm tra xem candidate có khác null không trước khi gọi getCandidateId
        if (existingSchedule != null && existingSchedule.getCandidateId() != null) {
            if (!existingSchedule.getCandidateId().equals(interviewScheduleDTO.getCandidateId())) {
                // Kiểm tra candidate mới đã tồn tại trong lịch phỏng vấn khác chưa
                if (interviewScheduleService.existsByCandidateId(interviewScheduleDTO.getCandidateId())) {
                    bindingResult.rejectValue("candidateId", "candidateId.exists", "Candidate already scheduled!");
                }
            }
        } else {
            // Xử lý trường hợp không tìm thấy candidate hoặc existingSchedule là null
            bindingResult.rejectValue("candidateId", "candidateId.notFound", "Candidate not found!");
        }
        // Kiểm tra nếu interviewTitle đã tồn tại
        if (!existingSchedule.getInterviewTitle().equals(interviewScheduleDTO.getInterviewTitle())) {
            // Kiểm tra candidate mới đã tồn tại trong lịch phỏng vấn khác chưa{
            if (interviewScheduleService.isInterviewTitleExists(interviewScheduleDTO.getInterviewTitle())) {

                bindingResult.rejectValue("interviewTitle", "interviewTitle.exists", "Interview title already exists!");

            }
        }

        if (interviewScheduleDTO.getScheduleFrom().isAfter(interviewScheduleDTO.getScheduleTo())) {

            bindingResult.rejectValue("scheduleFrom", "scheduleFrom.invalid",
                    "Schedule to must be after schedule from!");

        }


        if (bindingResult.hasErrors()) {
            // Nếu có lỗi, giữ lại các lựa chọn cho các trường
            List<UserDTO> interviewers = userService.getInterviewers();
            model.addAttribute("interviewers", interviewers);

            List<CandidateDTO> candidates = candidateService.getAllCandidates();
            model.addAttribute("candidates", candidates);

            List<Job> jobs = jobService.getAllJobsOpen();
            model.addAttribute("jobs", jobs);

            List<UserDTO> recruiters = userService.getRecruiters();
            model.addAttribute("recruiters", recruiters);

            return "contents/interviewschedule/schedule_edit"; // Trả lại form nếu có lỗi
        }

        // Lấy bản ghi lịch phỏng vấn cũ từ cơ sở dữ liệu theo ID

        // Tiến hành cập nhật lịch phỏng vấn nếu không có lỗi
        var result = interviewScheduleService.update(id, interviewScheduleDTO);
        if (result == null) {
            model.addAttribute("errorPopupMessage", "Failed to created interview schedule");
            // Nếu không cập nhật thành công, ném lỗi
            throw new IllegalArgumentException("Failed to create schedule");
        }

        // Thêm thông báo thành công vào RedirectAttributes
        redirectAttributes.addFlashAttribute("successMessage", "Change has been successfully updated!");

        // Redirect về trang quản lý sau khi thêm thành công
        return "redirect:/interview-schedule/index";
    }

    @GetMapping("/filter")
    public String filterSchedules(
            @RequestParam(required = false) String keyword,
            @RequestParam(name = "interviewerId", required = false) Long interviewerId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        int size = 10;
        var pageable = PageRequest.of(page, size);

        var interviewScheduleDTOs = interviewScheduleService.filterSchedule(keyword, interviewerId, status, pageable);

        model.addAttribute("interviewScheduleDTOs", interviewScheduleDTOs);
        model.addAttribute("keyword", keyword);
        model.addAttribute("interviewerId", interviewerId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", interviewScheduleDTOs.getTotalPages());
        model.addAttribute("totalUsers", interviewScheduleDTOs.getTotalElements());

        return "fragments/interview-schedule-table :: interviewScheduleTable";
    }

}
