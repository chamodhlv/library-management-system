package com.chamodh.library_management_system.repository;

import com.chamodh.library_management_system.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByMemberId(Long memberId);
    // "All borrow records for this member" - e.g. their borrowing history

    List<BorrowRecord> findByBookId(Long bookId);
    // "All borrow records for this book" - e.g. who's borrowed it over time

    List<BorrowRecord> findByReturnDateIsNull();
    // Currently-borrowed books (not yet returned) across the whole library -
    // useful for an admin dashboard or overdue-check job later
}