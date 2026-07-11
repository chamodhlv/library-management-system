package com.chamodh.library_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "borrow_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    // @ManyToOne = BorrowRecord OWNS this relationship - this creates the member_id
    // foreign key column directly on the borrow_records table
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDate borrowDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    // nullable - a book that hasn't been returned yet has no returnDate
    private LocalDate returnDate;

    @Column(precision = 10, scale = 2)
    // precision/scale for BigDecimal - up to 10 total digits, 2 after the decimal point
    // e.g. supports up to 99999999.99
    private BigDecimal fineAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    // STRING stores "PENDING" in the DB column, not an int (0,1,2) -
    // makes the raw table data human-readable and safe if you reorder the enum later
    @Column(nullable = false)
    private FineStatus fineStatus = FineStatus.NONE;

    public enum FineStatus {
        NONE, PENDING, PAID
    }
}