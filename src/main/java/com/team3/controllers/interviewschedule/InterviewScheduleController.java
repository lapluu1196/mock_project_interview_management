// package com.team3.controllers.interviewschedule;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import com.team3.dtos.candidate.CandidateDTO;
// import com.team3.dtos.interviewschedule.InterviewScheduleCreateDTO;
// import com.team3.dtos.interviewschedule.InterviewScheduleDTO;
// import com.team3.dtos.user.UserDTO;
// import com.team3.entities.Candidate;
// import com.team3.entities.InterviewSchedule;
// import com.team3.entities.Job;
// import com.team3.entities.User;
// import com.team3.services.CandidateService;
// import com.team3.services.InterviewScheduleService;
// import com.team3.services.JobService;
// import com.team3.services.UserService;

// import jakarta.validation.Valid;

// @Controller
// @RequestMapping("/interview-schedule")
// public class InterviewScheduleController {

//     @Autowired
//     private InterviewScheduleService interviewScheduleService;

//     @Autowired
//     private CandidateService candidateService;

//     @Autowired
//     private UserService userService;

//     @Autowired
//     private JobService jobService;

//     @GetMapping("/manager")
//     public String index(@RequestParam(required = false) String keyword,
//             @RequestParam(name = "interviewerId", required = false) Long interviewerId,
//             @RequestParam(name = "status", required = false) String status,
//             @RequestParam(defaultValue = "0") int page,
//             Model model) {
//         int size = 10;
//         ;
//         var pageable = PageRequest.of(page, size);
//         Page<InterviewScheduleDTO> interviewScheduleDTOs = interviewScheduleService.findAll(keyword, interviewerId,
//                 status, pageable);

//         List<User> interviewers = userService.getInterviewers();
//         List<InterviewSchedule> schedules = interviewScheduleService.getAllSchedulesWithInterviewers();
//         if (interviewScheduleDTOs.isEmpty()) {
//             model.addAttribute("message", "Data not found!");
//         }
//         model.addAttribute("schedules", schedules);
//         model.addAttribute("interviewers", interviewers);
//         model.addAttribute("interviewScheduleDTOs", interviewScheduleDTOs);
//         model.addAttribute("keyword", keyword);
//         model.addAttribute("currentPage", page);
//         model.addAttribute("totalPages", interviewScheduleDTOs.getTotalPages());
//         model.addAttribute("totalUsers", interviewScheduleDTOs.getTotalElements());
//         return "contents/interviewschedule/manager/schedule_list";
//     }

//     // Interviewer
//     @GetMapping("/interviewer")
//     public String indexInterviewer(@RequestParam(required = false) String keyword,
//             @RequestParam(name = "interviewerId", required = false) Long interviewerId,
//             @RequestParam(name = "status", required = false) String status,
//             @RequestParam(defaultValue = "0") int page,
//             Model model) {
//         int size = 10;
//         ;
//         var pageable = PageRequest.of(page, size);
//         Page<InterviewScheduleDTO> interviewScheduleDTOs = interviewScheduleService.findAll(keyword, interviewerId,
//                 status, pageable);

//         List<User> interviewers = userService.getInterviewers();
//         List<InterviewSchedule> schedules = interviewScheduleService.getAllSchedulesWithInterviewers();
//         if (interviewScheduleDTOs.isEmpty()) {
//             model.addAttribute("message", "Data not found!");
//         }
//         model.addAttribute("schedules", schedules);
//         model.addAttribute("interviewers", interviewers);
//         model.addAttribute("interviewScheduleDTOs", interviewScheduleDTOs);
//         model.addAttribute("keyword", keyword);
//         model.addAttribute("currentPage", page);
//         model.addAttribute("totalPages", interviewScheduleDTOs.getTotalPages());
//         model.addAttribute("totalUsers", interviewScheduleDTOs.getTotalElements());
//         return "contents/interviewschedule/interviewer/interviewer_schedule_list";
//     }

//     @GetMapping("/add")
//     public String addInterviewSchedule(Model model) {
//         var interviewCreateDTO = new InterviewScheduleCreateDTO();
//         model.addAttribute("interviewScheduleCreateDTO", interviewCreateDTO);
//         List<User> interviewers = userService.getInterviewers();
//         model.addAttribute("interviewers", interviewers);
//         var candidates = candidateService.getAllCandidates();
//         model.addAttribute("candidates", candidates);

//         var jobs = jobService.getAllJobsOpen();
//         model.addAttribute("jobs", jobs);

//         var recruiters = userService.getRecruiters();
//         model.addAttribute("recruiters", recruiters);
//         return "contents/interviewschedule/interviewer/schedule_create";
//     }

//     @PostMapping("/add")
//     public String addInterviewSchedule(
//             @ModelAttribute @Valid InterviewScheduleCreateDTO interviewScheduleCreateDTO,
//             BindingResult bindingResult,
//             RedirectAttributes redirectAttributes,
//             Model model) {

//         // Kiểm tra nếu interviewTitle đã tồn tại
//         if (interviewScheduleService.isInterviewTitleExists(interviewScheduleCreateDTO.getInterviewTitle())) {
//             model.addAttribute("errorPopupMessage", "Failed to created interview schedule");

//             bindingResult.rejectValue("interviewTitle", "interviewTitle.exists", "Interview title already exists!");

//         }

//         if (interviewScheduleCreateDTO.getScheduleFrom().isAfter(interviewScheduleCreateDTO.getScheduleTo())) {
//             model.addAttribute("errorPopupMessage", "Failed to created interview schedule");

//             bindingResult.rejectValue("scheduleFrom", "scheduleFrom.invalid",
//                     "Schedule to must be after schedule from!");

//         }

//         if (interviewScheduleService.existsByCandidateId(interviewScheduleCreateDTO.getCandidateId())) {
//             model.addAttribute("errorPopupMessage", "Failed to created interview schedule");

//             bindingResult.rejectValue("candidateId", "candidateId.exists", "Candidate already scheduled!");
//         }
//         // model.addAttribute("previousPageUrl",
//         // "/interview-schedule/manager/schedule_list");
//         // Kiểm tra BindingResult có lỗi không
//         if (bindingResult.hasErrors()) {
//             // Nếu có lỗi, giữ lại các lựa chọn cho các trường
//             model.addAttribute("errorPopupMessage", "Failed to created interview schedule");

//             List<User> interviewers = userService.getInterviewers();
//             model.addAttribute("interviewers", interviewers);

//             List<CandidateDTO> candidates = candidateService.getAllCandidates();
//             model.addAttribute("candidates", candidates);

//             List<Job> jobs = jobService.getAllJobsOpen();
//             model.addAttribute("jobs", jobs);

//             List<User> recruiters = userService.getRecruiters();
//             model.addAttribute("recruiters", recruiters);

//             return "contents/interviewschedule/interviewer/schedule_create"; // Trả lại form nếu có lỗi
//         }

//         // Tiến hành tạo lịch phỏng vấn nếu không có lỗi
//         var result = interviewScheduleService.create(interviewScheduleCreateDTO);
//         if (result == null) {
//             // Nếu không tạo thành công, ném lỗi
//             throw new IllegalArgumentException("Failed to create schedule");
//         }

//         // Thêm thông báo thành công vào RedirectAttributes
//         redirectAttributes.addFlashAttribute("successMessage", "Successfully created schedule!");

//         // Redirect về trang quản lý sau khi thêm thành công
//         return "redirect:/interview-schedule/manager";
//     }

//     @GetMapping("manager/scheduleDetail")
//     public String scheduleDetail(Model model) {
//         return "contents/interviewschedule/manager/schedule_detail";
//     }

//     @GetMapping("interviewer/scheduleDetail")
//     public String scheduleDetailInterviewer(Model model) {
//         return "contents/interviewschedule/interviewer/interviewer_schedule_detail";
//     }

//     @GetMapping("/edit")
//     public String updateInterviewSchedule(Model model) {
//         return "contents/interviewschedule/manager/schedule_edit";
//     }

// }
