package com.chamodh.library_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    // unique = true - two categories can't have the same name, e.g. no duplicate "Fiction" rows
    private String name;

    @ManyToMany(mappedBy = "categories")
    // Same idea as Author - Book owns this relationship via @JoinTable,
    // Category is just the inverse side pointing back
    private Set<Book> books = new HashSet<>();
}