package com.team3.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;

    private String username;

    @NotBlank(message = "Full name is required!")
    private String fullName;

    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "Invalid email address! (eg. 8hQkA@example.com)")
    private String email;

    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender is required!")
    private String gender;

    @NotBlank(message = "Department is required!")
    private String department;

    @Pattern(regexp = "^(Admin|Manager|Recruiter|Interviewer)$", message = "Role is required!")
    private String role;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private String notes;

    private String address;

    @Pattern(regexp = "^(Active|Inactive)$", message = "Status is required!")
    private String status;

}
