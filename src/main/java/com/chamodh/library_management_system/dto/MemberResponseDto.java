package com.chamodh.library_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate membershipDate;
    // CRITICALLY: no password field here. Even the hashed version should
    // never be sent back to the client in an API response.
}