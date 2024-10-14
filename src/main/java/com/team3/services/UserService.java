package com.team3.services;

import com.team3.dtos.user.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<UserDTO> findAll(String search, Pageable pageable);

    UserDTO findById(Long id);

    String save(UserDTO userDTO);

    String update(UserDTO userDTO);

    void deleteById(Long id);

    Page<UserDTO> filterUser(String search, String role, Pageable pageable);

    String updateStatus(Long id);
}
