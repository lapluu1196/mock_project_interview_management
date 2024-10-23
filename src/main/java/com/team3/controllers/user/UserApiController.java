package com.team3.controllers.user;

import com.team3.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    @Autowired
    private UserService userService;

    @PostMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestParam("id") Long id) {
        try {
            String newStatus = userService.updateStatus(id);
            return ResponseEntity.ok(newStatus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/current-user-fullname")
    @ResponseBody
    public ResponseEntity<?> getCurrentUserFullName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Kiểm tra nếu người dùng có vai trò Recruiter
            if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("Recruiter"))) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                // Giả sử bạn có userFullName lưu trữ trong UserDetails hoặc có thể tìm từ cơ sở dữ liệu
                String userFullName = userService.findByUsername(userDetails.getUsername()).getFullName(); // Hàm tìm userFullName dựa trên username

                return ResponseEntity.ok(userFullName); // Trả về userFullName của người dùng
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be a Recruiter to assign yourself.");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
