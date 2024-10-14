package com.team3.controllers.user;

import com.team3.dtos.user.UserDTO;
import com.team3.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String userList(@RequestParam(required = false) String search,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {
        int size = 10;;
        var pageable = PageRequest.of(page, size);
        var userDTOs = userService.findAll(search, pageable);

        model.addAttribute("userDTOs", userDTOs);
        model.addAttribute("keyword", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userDTOs.getTotalPages());
        model.addAttribute("totalUsers", userDTOs.getTotalElements());

        return "contents/user/user-list";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        UserDTO newUserDTO = new UserDTO();

        model.addAttribute("userDTO", newUserDTO);

        return "contents/user/user-create";
    }

    @PostMapping("/add")
    public String createUser(@ModelAttribute @Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "contents/user/user-create";
        }

        String result = userService.save(userDTO);

        redirectAttributes.addFlashAttribute("result", result);

        return "redirect:/users";
    }

    @GetMapping("/filter")
    public String filterUser(@RequestParam(required = false) String search,
                             @RequestParam(required = false) String role,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {
        int size = 10;
        var pageable = PageRequest.of(page, size);

        var userDTOs = userService.filterUser(search, role, pageable);


        model.addAttribute("userDTOs", userDTOs);
        model.addAttribute("keyword", search);
        model.addAttribute("role", role);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userDTOs.getTotalPages());
        model.addAttribute("totalUsers", userDTOs.getTotalElements());

        return "fragments/user-table :: userTable";
    }

    @GetMapping("detail/{id}")
    public String userDetail(@PathVariable("id") Long id, Model model) {

        UserDTO userDTO = userService.findById(id);

        model.addAttribute("userDTO", userDTO);

        return "contents/user/user-detail";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {

        UserDTO userDTO = userService.findById(id);

        model.addAttribute("userDTO", userDTO);

        return "contents/user/user-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute @Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "contents/user/user-edit";
        }

        try {
            String result = userService.update(userDTO);

            if (result != null) {
                redirectAttributes.addFlashAttribute("result", result);
            }

            return "redirect:/users";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users/edit/{id}";
        }
    }

}
