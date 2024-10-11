package com.team3.services.impl;

import com.team3.dtos.user.UserDTO;
import com.team3.entities.User;
import com.team3.repositories.UserRepository;
import com.team3.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserDTO> findAll(String keyword, Pageable pageable) {
        Specification<User> spec = (root, query, criteriaBuilder) -> {
            if (keyword == null) {
                return null;
            }

            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("username"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("email"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("phoneNumber"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("role"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("status"), "%" + keyword + "%")
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
    public UserDTO save(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
