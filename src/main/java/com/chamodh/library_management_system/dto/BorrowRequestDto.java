package com.chamodh.library_management_system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BorrowRequestDto {

    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    // No borrowDate, dueDate, fineAmount, fineStatus here -
    // the SERVICE layer sets these automatically (today's date, +14 days, etc.)
    // The client just says WHO is borrowing WHAT.
}