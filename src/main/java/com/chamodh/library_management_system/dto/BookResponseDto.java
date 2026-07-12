package com.chamodh.library_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDto {
    private Long id;
    private String title;
    private String isbn;
    private int totalCopies;
    private int availableCopies;
    private Set<AuthorSummaryDto> authors;
    private Set<CategorySummaryDto> categories;
}