package com.chamodh.library_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private int totalCopies;

    @Column(nullable = false)
    private int availableCopies;

    @ManyToMany
    @JoinTable(
            name = "book_authors",                              // the actual join table Hibernate creates in Postgres
            joinColumns = @JoinColumn(name = "book_id"),         // FK column pointing back to this Book
            inverseJoinColumns = @JoinColumn(name = "author_id") // FK column pointing to the related Author
    )
    // Book is the OWNING side here - this @JoinTable annotation is what
    // actually creates the join table. Author's `mappedBy = "authors"` just points here.
    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    // mappedBy = "book" - BorrowRecord owns this relationship (it'll have a @ManyToOne back to Book)
    // cascade = ALL - if a Book gets deleted, its borrow records get deleted too (careful with this in production,
    // but fine for a portfolio project - we'll discuss soft-deletes later if you want)
    private List<BorrowRecord> borrowRecords = new ArrayList<>();
}