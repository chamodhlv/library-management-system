package com.chamodh.library_management_system.dto;

import com.chamodh.library_management_system.entity.BorrowRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecordResponseDto {
    private Long id;
    private MemberSummaryDto member;
    private BookSummaryDto book;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BigDecimal fineAmount;
    private BorrowRecord.FineStatus fineStatus;

    // COMPUTED field - doesn't exist as a column in the database at all.
    // We calculate it here: true if not yet returned AND past the due date.
    private boolean overdue;
}