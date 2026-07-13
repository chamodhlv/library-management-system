package com.chamodh.library_management_system.service;

import com.chamodh.library_management_system.dto.*;
import com.chamodh.library_management_system.entity.Book;
import com.chamodh.library_management_system.entity.BorrowRecord;
import com.chamodh.library_management_system.entity.Member;
import com.chamodh.library_management_system.repository.BookRepository;
import com.chamodh.library_management_system.repository.BorrowRecordRepository;
import com.chamodh.library_management_system.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowRecordService {

    private static final int BORROW_PERIOD_DAYS = 14;
    private static final BigDecimal FINE_PER_DAY = BigDecimal.valueOf(50);
    // Constants at the top - if these ever need to change (e.g. library
    // policy changes to 21 days or Rs.75/day), there's exactly ONE place to edit

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public BorrowRecordResponseDto borrowBook(BorrowRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + requestDto.getMemberId()));

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + requestDto.getBookId()));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No available copies of this book to borrow");
        }
        // BUSINESS RULE: can't borrow what isn't available

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        // Decrement FIRST - if this book had 1 copy left, the next person
        // to try borrowing it will correctly see 0 available

        BorrowRecord record = new BorrowRecord();
        record.setMember(member);
        record.setBook(book);
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(BORROW_PERIOD_DAYS));
        // returnDate stays null - book hasn't been returned yet
        // fineAmount defaults to BigDecimal.ZERO, fineStatus defaults to NONE
        // (both set as field defaults in the entity itself)

        BorrowRecord saved = borrowRecordRepository.save(record);
        return mapToResponseDto(saved);
    }

    public BorrowRecordResponseDto returnBook(Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new RuntimeException("Borrow record not found with id: " + borrowRecordId));

        if (record.getReturnDate() != null) {
            throw new RuntimeException("This book has already been returned");
        }
        // BUSINESS RULE: can't return something already returned

        LocalDate today = LocalDate.now();
        record.setReturnDate(today);

        if (today.isAfter(record.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(record.getDueDate(), today);
            BigDecimal fine = FINE_PER_DAY.multiply(BigDecimal.valueOf(daysOverdue));
            record.setFineAmount(fine);
            record.setFineStatus(BorrowRecord.FineStatus.PENDING);
            // Fine is calculated but marked PENDING - a separate "pay fine"
            // operation later will change this to PAID
        }
        // If returned on time, fineAmount stays ZERO and fineStatus stays NONE
        // (whatever they were set to when the record was created)

        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
        // Increment the copy count back - this book is available again

        BorrowRecord updated = borrowRecordRepository.save(record);
        return mapToResponseDto(updated);
    }

    public BorrowRecordResponseDto getBorrowRecordById(Long id) {
        BorrowRecord record = borrowRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow record not found with id: " + id));
        return mapToResponseDto(record);
    }

    public List<BorrowRecordResponseDto> getRecordsByMember(Long memberId) {
        return borrowRecordRepository.findByMemberId(memberId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public List<BorrowRecordResponseDto> getRecordsByBook(Long bookId) {
        return borrowRecordRepository.findByBookId(bookId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public List<BorrowRecordResponseDto> getCurrentlyBorrowed() {
        return borrowRecordRepository.findByReturnDateIsNull()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    private BorrowRecordResponseDto mapToResponseDto(BorrowRecord record) {
        MemberSummaryDto memberDto = new MemberSummaryDto(
                record.getMember().getId(),
                record.getMember().getName(),
                record.getMember().getEmail()
        );

        BookSummaryDto bookDto = new BookSummaryDto(
                record.getBook().getId(),
                record.getBook().getTitle(),
                record.getBook().getIsbn()
        );

        boolean isOverdue = record.getReturnDate() == null
                && LocalDate.now().isAfter(record.getDueDate());
        // COMPUTED field: overdue only makes sense for books NOT YET returned.
        // A returned book is never "overdue" even if it was returned late -
        // that's captured by fineAmount/fineStatus instead

        return new BorrowRecordResponseDto(
                record.getId(),
                memberDto,
                bookDto,
                record.getBorrowDate(),
                record.getDueDate(),
                record.getReturnDate(),
                record.getFineAmount(),
                record.getFineStatus(),
                isOverdue
        );
    }
}