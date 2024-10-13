package com.team3.services;

import com.team3.dtos.user.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<UserDTO> findAll(String keyword, Pageable pageable);

    UserDTO findById(Long id);

    String save(UserDTO userDTO);

    UserDTO update(UserDTO userDTO);

    void deleteById(Long id);
}
