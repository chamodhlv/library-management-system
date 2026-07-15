package com.chamodh.library_management_system.dto;

import com.chamodh.library_management_system.entity.Member;
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
    private Member.Role role;
    // ADDED - needed for the frontend to display and toggle roles
}