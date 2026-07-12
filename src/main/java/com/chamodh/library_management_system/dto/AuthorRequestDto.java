package com.chamodh.library_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthorRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    private String bio;
    // bio is optional, so no validation annotation needed
}