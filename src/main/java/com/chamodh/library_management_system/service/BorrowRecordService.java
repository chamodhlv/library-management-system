package com.chamodh.library_management_system.service;

import com.chamodh.library_management_system.dto.*;
import com.chamodh.library_management_system.entity.Book;
import com.chamodh.library_management_system.entity.BorrowRecord;
import com.chamodh.library_management_system.entity.Member;
import com.chamodh.library_management_system.exception.BusinessRuleException;
import com.chamodh.library_management_system.exception.ResourceNotFoundException;
import com.chamodh.library_management_system.repository.BookRepository;
import com.chamodh.library_management_system.repository.BorrowRecordRepository;
import com.chamodh.library_management_system.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowRecordService {

    private static final int BORROW_PERIOD_DAYS = 14;
    private static final BigDecimal FINE_PER_DAY = BigDecimal.valueOf(50);

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BorrowRecordResponseDto borrowBook(BorrowRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + requestDto.getMemberId()));

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + requestDto.getBookId()));

        if (book.getAvailableCopies() <= 0) {
            throw new BusinessRuleException("No available copies of this book to borrow");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BorrowRecord record = new BorrowRecord();
        record.setMember(member);
        record.setBook(book);
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(BORROW_PERIOD_DAYS));

        BorrowRecord saved = borrowRecordRepository.save(record);
        return mapToResponseDto(saved);
    }

    @Transactional
    public BorrowRecordResponseDto returnBook(Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with id: " + borrowRecordId));

        if (record.getReturnDate() != null) {
            throw new BusinessRuleException("This book has already been returned");
        }

        LocalDate today = LocalDate.now();
        record.setReturnDate(today);

        if (today.isAfter(record.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(record.getDueDate(), today);
            BigDecimal fine = FINE_PER_DAY.multiply(BigDecimal.valueOf(daysOverdue));
            record.setFineAmount(fine);
            record.setFineStatus(BorrowRecord.FineStatus.PENDING);
        }

        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        BorrowRecord updated = borrowRecordRepository.save(record);
        return mapToResponseDto(updated);
    }

    public BorrowRecordResponseDto getBorrowRecordById(Long id) {
        BorrowRecord record = borrowRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with id: " + id));
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