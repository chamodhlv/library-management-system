package com.chamodh.library_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDto {
    private Long id;
    private String name;
    private String bio;
    // We deliberately do NOT include the list of books here -
    // fetching "all books by this author" would be a separate endpoint
    // (e.g. GET /authors/{id}/books), not bloat this response.
}