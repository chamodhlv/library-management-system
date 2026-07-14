package com.chamodh.library_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String email;
    private String role;
    // Sending back email/role too - saves the frontend an extra API call
    // just to know who's logged in and what they can do
}