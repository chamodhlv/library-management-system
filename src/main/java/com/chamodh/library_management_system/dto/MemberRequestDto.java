package com.chamodh.library_management_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    // This will be the RAW password from the client -
    // the service layer hashes it with BCrypt before saving. NEVER save this as-is.

    private String phoneNumber;
    // No membershipDate here - the service sets it to today's date automatically
    // when a member registers
}