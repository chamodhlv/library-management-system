package com.chamodh.library_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Set;

@Data
public class BookRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @Positive(message = "Total copies must be greater than 0")
    private int totalCopies;

    // Client sends author/category IDs, not full objects -
    // we look these up in the service layer before saving
    private Set<Long> authorIds;
    private Set<Long> categoryIds;
}