package com.team3.dtos.user;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;

    private String username;

    private String fullName;

    private String email;

    private String gender;

    private String department;

    private String role;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private String notes;

    private String address;

    private String status;

}
