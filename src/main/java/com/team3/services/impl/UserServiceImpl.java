package com.team3.services.impl;

import com.team3.dtos.user.EmailDTO;
import com.team3.dtos.user.UserDTO;
import com.team3.entities.User;
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

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


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
        return null;
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

            String result = emailService.sendEmail(emailDTO);

            return "Successfully created user!";
        }
        return "Failed to create user!";
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

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


}
