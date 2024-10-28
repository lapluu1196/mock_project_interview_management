package com.team3.repositories;

import com.team3.dtos.user.UserDTO;
import com.team3.entities.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    List<User> findByRole(String role);

    User findByEmail(String email);

    User findByUsername(String username);

    // Minh
    @Query("SELECT u FROM User u WHERE u.role = 'MANAGER'")
    List<User> findAllManagers();

    @Query("SELECT u FROM User u WHERE u.role = 'RECRUITER'")
    List<User> findAllRecruiters();

    // Long
    @Query("Select u.role from User u where u.userId = :userID")
    String getUserRole(@Param("userID") Long userID);


}
