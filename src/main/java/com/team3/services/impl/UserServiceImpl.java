package com.team3.services.impl;

import com.team3.dtos.user.EmailDTO;
import com.team3.dtos.user.UserDTO;
import com.team3.entities.PasswordResetToken;
import com.team3.entities.User;
import com.team3.repositories.PasswordResetTokenRepository;
import com.team3.repositories.UserRepository;
import com.team3.services.EmailService;
import com.team3.services.UserService;
import com.team3.utils.PasswordGenerateUtil;
import com.team3.utils.UsernameGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;


    @Override
    public Page<UserDTO> findAll(String search, Pageable pageable) {
        Specification<User> spec = (root, query, criteriaBuilder) -> {
            if (search == null) {
                return null;
            }

            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("username"), "%" + search + "%"),
                    criteriaBuilder.like(root.get("email"), "%" + search + "%"),
                    criteriaBuilder.like(root.get("phoneNumber"), "%" + search + "%"),
                    criteriaBuilder.like(root.get("role"), "%" + search + "%"),
                    criteriaBuilder.like(root.get("status"), "%" + search + "%")
            );
        };

        var users = userRepository.findAll(spec, pageable);

        return users.map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setGender(user.getGender());
            userDTO.setDepartment(user.getDepartment());
            userDTO.setRole(user.getRole());
            userDTO.setDateOfBirth(user.getDateOfBirth());
            userDTO.setAddress(user.getAddress());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setStatus(user.getStatus());
            userDTO.setNotes(user.getNotes());
            return userDTO;
        });
    }

    @Override
    public UserDTO findById(Long id) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setGender(user.getGender());
        userDTO.setDepartment(user.getDepartment());
        userDTO.setRole(user.getRole());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setStatus(user.getStatus());
        userDTO.setNotes(user.getNotes());

        return userDTO;
    }

    @Override
    public String save(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("Email has been used!");
        }

        List<User> users = userRepository.findAll();

        long nextId = 0L;

        if (users.isEmpty()) {
            nextId = 1L;
        } else {
            nextId = users.getLast().getUserId() + 1;
        }

        String password = PasswordGenerateUtil.passwordGenerate();

        User user = new User();
        user.setUsername(UsernameGenerateUtil.usernameGenerate(userDTO.getFullName(), nextId));
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setGender(userDTO.getGender());
        user.setDepartment(userDTO.getDepartment());
        user.setRole(userDTO.getRole());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setAddress(userDTO.getAddress());
        user.setStatus(userDTO.getStatus());
        user.setNotes(userDTO.getNotes());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        if (savedUser.getUserId() != null) {
            EmailDTO emailDTO = EmailDTO.builder()
                    .subject("no-reply-email-IMS-system")
                    .from("interviewmanagementsystem.team3@gmail.com")
                    .to(user.getEmail())
                    .data(Map.of("username", user.getUsername(), "password", password))
                    .build();

            String result = emailService.sendEmail(emailDTO, "email-user-create-template");

            return "Successfully created user!";
        }
        return "Failed to create user!";
    }

    @Override
    public String update(UserDTO userDTO) {

        User user = userRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        // Kiểm tra nếu email thay đổi, và nếu có trùng lặp với người dùng khác thì ném ngoại lệ
        if (!user.getEmail().equals(userDTO.getEmail())) {
            User userWithSameEmail = userRepository.findByEmail(userDTO.getEmail());
            // Kiểm tra nếu email đã được sử dụng bởi một người dùng khác, không phải chính người dùng hiện tại
            if (userWithSameEmail != null && !userWithSameEmail.getUserId().equals(user.getUserId())) {
                throw new IllegalArgumentException("Email has been used!");
            }

            user.setEmail(userDTO.getEmail());
        }

        // Cập nhật các thông tin khác
        user.setFullName(userDTO.getFullName());
        user.setGender(userDTO.getGender());
        user.setDepartment(userDTO.getDepartment());
        user.setRole(userDTO.getRole());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setAddress(userDTO.getAddress());
        user.setStatus(userDTO.getStatus());
        user.setNotes(userDTO.getNotes());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        if (savedUser.getUserId() != null) {
            return "Change has been successfully updated!";
        }

        return "Fail to updated change!";
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<UserDTO> getInterviewers() {
        List<User> users = userRepository.findByRole("Interviewer");

        if (!users.isEmpty()) {
            return users.stream().map(user -> {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(user.getUserId());
                userDTO.setUsername(user.getUsername());
                userDTO.setFullName(user.getFullName());
                userDTO.setEmail(user.getEmail());
                userDTO.setGender(user.getGender());
                userDTO.setDepartment(user.getDepartment());
                userDTO.setRole(user.getRole());
                userDTO.setDateOfBirth(user.getDateOfBirth());
                userDTO.setAddress(user.getAddress());
                userDTO.setPhoneNumber(user.getPhoneNumber());
                userDTO.setStatus(user.getStatus());
                userDTO.setNotes(user.getNotes());
                return userDTO;
            }).collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public Page<UserDTO> filterUser(String search, String role, Pageable pageable) {
        Specification<User> spec = (Specification<User>) (root, query, criteriaBuilder) -> {
            if ((search == null || search.isEmpty()) && (role == null || role.isEmpty())) {
                return null;
            }

            if (search == null || search.isEmpty()) {
                return criteriaBuilder.equal(root.get("role"), role);
            }

            return criteriaBuilder.and(
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("username"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("email"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("phoneNumber"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("role"), "%" + search + "%"),
                            criteriaBuilder.like(root.get("status"), "%" + search + "%")
                    ),
                    criteriaBuilder.equal(root.get("role"), role)
            );
        };

        var users = userRepository.findAll(spec, pageable);

        return users.map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setGender(user.getGender());
            userDTO.setDepartment(user.getDepartment());
            userDTO.setRole(user.getRole());
            userDTO.setDateOfBirth(user.getDateOfBirth());
            userDTO.setAddress(user.getAddress());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setStatus(user.getStatus());
            userDTO.setNotes(user.getNotes());
            return userDTO;
        });
    }

    @Override
    public String updateStatus(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        if ("Active".equals(user.getStatus())) {
            user.setStatus("Inactive");
        } else {
            user.setStatus("Active");
        }

        userRepository.save(user);

        return user.getStatus();
    }

    @Override
    public UserDTO findByUsername(String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setGender(user.getGender());
        userDTO.setDepartment(user.getDepartment());
        userDTO.setRole(user.getRole());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setStatus(user.getStatus());
        userDTO.setNotes(user.getNotes());

        return userDTO;
    }

    @Override
    public UserDTO findByEmail(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setGender(user.getGender());
        userDTO.setDepartment(user.getDepartment());
        userDTO.setRole(user.getRole());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setStatus(user.getStatus());
        userDTO.setNotes(user.getNotes());

        return userDTO;
    }

    @Override
    public void createPasswordResetTokenForUser(String email, String resetUrl, String token) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }

        // save token
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(1440));

        passwordResetTokenRepository.save(passwordResetToken);

        // send email
        EmailDTO emailDTO = EmailDTO.builder()
                .subject("Password Reset")
                .from("interviewmanagementsystem.team3@gmail.com")
                .to(user.getEmail())
                .data(Map.of("resetEmail", email, "resetUrl", resetUrl))
                .build();
        String result = emailService.sendEmail(emailDTO, "email-user-password-reset-template");
    }

    @Override
    public String updatePassword(Long id, String password) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        String oldPassword = user.getPassword();

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        String newPassword = user.getPassword();

        if (!oldPassword.equals(newPassword)) {
            return "Password has been updated successfully!";
        }

        return "Password has been updated failed!";
    }

}
