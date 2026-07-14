package com.chamodh.library_management_system.controller;

import com.chamodh.library_management_system.dto.BorrowRecordResponseDto;
import com.chamodh.library_management_system.dto.BorrowRequestDto;
import com.chamodh.library_management_system.service.BorrowRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow-records")
@RequiredArgsConstructor
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;

    @PostMapping("/borrow")
    public ResponseEntity<BorrowRecordResponseDto> borrowBook(@Valid @RequestBody BorrowRequestDto requestDto) {
        // POST /api/borrow-records/borrow - an ACTION, not a generic "create"
        BorrowRecordResponseDto record = borrowRecordService.borrowBook(requestDto);
        return new ResponseEntity<>(record, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<BorrowRecordResponseDto> returnBook(@PathVariable Long id) {
        // PATCH /api/borrow-records/5/return - partially updates the record
        // (sets returnDate, fineAmount, fineStatus) without replacing it entirely
        BorrowRecordResponseDto record = borrowRecordService.returnBook(id);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowRecordResponseDto> getBorrowRecordById(@PathVariable Long id) {
        BorrowRecordResponseDto record = borrowRecordService.getBorrowRecordById(id);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BorrowRecordResponseDto>> getRecordsByMember(@PathVariable Long memberId) {
        List<BorrowRecordResponseDto> records = borrowRecordService.getRecordsByMember(memberId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BorrowRecordResponseDto>> getRecordsByBook(@PathVariable Long bookId) {
        List<BorrowRecordResponseDto> records = borrowRecordService.getRecordsByBook(bookId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/currently-borrowed")
    public ResponseEntity<List<BorrowRecordResponseDto>> getCurrentlyBorrowed() {
        List<BorrowRecordResponseDto> records = borrowRecordService.getCurrentlyBorrowed();
        return ResponseEntity.ok(records);
    }
}