package com.team3.services;

import com.team3.dtos.user.UserDTO;
import com.team3.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<UserDTO> findAll(String search, Pageable pageable);

    UserDTO findById(Long id);

    String save(UserDTO userDTO);

    String update(UserDTO userDTO);

    void deleteById(Long id);

    List<UserDTO> getInterviewers();

    Page<UserDTO> filterUser(String search, String role, Pageable pageable);

    String updateStatus(Long id);

    UserDTO findByUsername(String username);


    List<UserDTO> getRecruiters();


    UserDTO findByEmail(String email);

    void createPasswordResetTokenForUser(String email, String resetUrl, String token);

    String updatePassword(Long id, String password);

    // Minh
    List<User> getAllManagers();

    List<User> getAllRecruiters();

    User getUser(Long id);

    // Long add phuong thuc lay User
    String getUserRole(Long userID); // Gets role based on userID
    boolean isAuthorized(Long userID, String... roles); // Checks if user has one of the roles
}
