package com.chamodh.library_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSummaryDto {
    private Long id;
    private String name;
    private String email;
    // No password, no borrowRecords - just enough to display "borrowed by: [name]"
}