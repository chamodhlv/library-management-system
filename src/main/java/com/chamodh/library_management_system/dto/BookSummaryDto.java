package com.chamodh.library_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSummaryDto {
    private Long id;
    private String title;
    private String isbn;
    // No authors, no categories, no copy counts - a borrow record just needs
    // to show WHICH book, not its full detail
}