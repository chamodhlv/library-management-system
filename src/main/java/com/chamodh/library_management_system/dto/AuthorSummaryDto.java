package com.chamodh.library_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorSummaryDto {
    private Long id;
    private String name;
    // No bio, no books - just enough to display "written by: [name]"
}