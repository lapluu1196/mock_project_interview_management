package com.team3.controllers.user;

import com.team3.dtos.user.UserDTO;
import com.team3.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String userList(@RequestParam(required = false) String keyword,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {
        int size = 10;;
        var pageable = PageRequest.of(page, size);
        var userDTOs = userService.findAll(keyword, pageable);

        model.addAttribute("userDTOs", userDTOs);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userDTOs.getTotalPages());
        model.addAttribute("totalUsers", userDTOs.getTotalElements());

        return "contents/user/user_list";
    }
}
