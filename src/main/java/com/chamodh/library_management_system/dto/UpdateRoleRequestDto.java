package com.chamodh.library_management_system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.chamodh.library_management_system.entity.Member;

@Data
public class UpdateRoleRequestDto {

    @NotNull(message = "Role is required")
    private Member.Role role;
}