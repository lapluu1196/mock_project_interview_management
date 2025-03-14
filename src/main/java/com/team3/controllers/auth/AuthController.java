package com.team3.controllers.auth;

import com.team3.dtos.user.UserDTO;
import com.team3.entities.PasswordResetToken;
import com.team3.services.EmailService;
import com.team3.services.PasswordResetTokenService;
import com.team3.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @GetMapping("/login")
    public String login() {

        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @GetMapping("/password/reset")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {

        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);
        if (passwordResetToken == null) {
            model.addAttribute("message", "Invalid Token");
            return "auth/login";
        }
        if (passwordResetToken.isExpired()) {
            model.addAttribute("message", "Token Expired");
            return "auth/login";
        }

        model.addAttribute("token", token);
        model.addAttribute("userId", passwordResetToken.getUser().getUserId());

        return "auth/reset-password";
    }

    @PostMapping("/password/reset")
    public String processResetPassword(HttpServletRequest request, @RequestParam("token") String token,
                                       @RequestParam("userId") Long userId, @RequestParam("password") String password,
                                       RedirectAttributes redirectAttributes, Model model) {

        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);
        if (passwordResetToken == null) {
            model.addAttribute("message", "Invalid Token");
            return "auth/reset-password";
        }

        if (passwordResetToken.isExpired()) {
            model.addAttribute("message", "Token Expired");
            return "auth/reset-password";
        }

        UserDTO userDTO = userService.findById(userId);

        if (userDTO == null) {
            redirectAttributes.addFlashAttribute("message", "Account Not Found");
            return "redirect:/auth/login";
        }

        String result = userService.updatePassword(userDTO.getUserId(), password);

        redirectAttributes.addFlashAttribute("message", result);

        return "redirect:/auth/login";
    }

}
