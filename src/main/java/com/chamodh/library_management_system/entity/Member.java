package com.chamodh.library_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    // Will store the BCrypt-hashed password once we wire up Spring Security -
    // never store plain text passwords, even in a portfolio project
    private String password;

    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate membershipDate;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    // Same pattern as Book - Member is the inverse side, BorrowRecord owns the relationship
    private List<BorrowRecord> borrowRecords = new ArrayList<>();
}