package com.team3.controllers.interviewschedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.team3.dtos.interviewschedule.InterviewScheduleDTO;
import com.team3.dtos.user.UserDTO;
import com.team3.entities.InterviewSchedule;
import com.team3.entities.User;
import com.team3.services.CandidateService;
import com.team3.services.InterviewScheduleService;
import com.team3.services.UserService;

@Controller
@RequestMapping("/interview-schedule")
public class InterviewScheduleController {

    @Autowired
    private InterviewScheduleService interviewScheduleService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private UserService userService;
    @GetMapping("/manager")
    public String index(@RequestParam(required = false) String keyword,
                        @RequestParam(name = "interviewerId", required = false) Long interviewerId,
                        @RequestParam(name = "status", required = false) String status,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {
        int size = 10;;
        var pageable = PageRequest.of(page, size);
        Page<InterviewScheduleDTO> interviewScheduleDTOs;
        if(interviewerId == null || status == null){
            interviewScheduleDTOs = interviewScheduleService.findAll(keyword, pageable);
        }else{
            interviewScheduleDTOs = interviewScheduleService.findAll(interviewerId, status ,pageable);
        }
        List<User> interviewers = userService.getInterviewers();
        List<InterviewSchedule> schedules = interviewScheduleService.getAllSchedulesWithInterviewers();
        if(interviewScheduleDTOs.isEmpty()){
            model.addAttribute("message", "Data not found!");
        }
        model.addAttribute("schedules", schedules);
        model.addAttribute("interviewers", interviewers);                    
        model.addAttribute("interviewScheduleDTOs", interviewScheduleDTOs);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", interviewScheduleDTOs.getTotalPages());
        model.addAttribute("totalUsers", interviewScheduleDTOs.getTotalElements());
        return "contents/interviewschedule/manager/schedule_list";
    }


    //Interviewer
    // @GetMapping("/interviewer")
    // public String indexInterviewer(@RequestParam(required = false) String keyword,
    //                        @RequestParam(defaultValue = "0") int page,
    //                        Model model) {
    //     int size = 10;;
    //     var pageable = PageRequest.of(page, size);
    //     var interviewScheduleDTOs = interviewScheduleService.findAll(keyword, pageable);
    //     List<User> interviewers = userService.getInterviewers();
    //      List<InterviewSchedule> schedules = interviewScheduleService.getAllSchedulesWithInterviewers();
    //     model.addAttribute("schedules", schedules);
    //     model.addAttribute("interviewers", interviewers);                    
    //     model.addAttribute("interviewScheduleDTOs", interviewScheduleDTOs);
    //     model.addAttribute("keyword", keyword);
    //     model.addAttribute("currentPage", page);
    //     model.addAttribute("totalPages", interviewScheduleDTOs.getTotalPages());
    //     model.addAttribute("totalUsers", interviewScheduleDTOs.getTotalElements());
    //     return "contents/interviewschedule/interviewer/interviewer_schedule_list";
    // }
}
