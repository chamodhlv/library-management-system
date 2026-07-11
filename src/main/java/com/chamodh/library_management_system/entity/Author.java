package com.chamodh.library_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
@Data                  // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor     // Lombok: empty constructor - JPA requires this
@AllArgsConstructor    // Lombok: constructor with all fields - handy for tests
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // IDENTITY = let Postgres auto-increment the id column (like AUTO_INCREMENT in MySQL)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    // length = 1000 lets Hibernate know to generate a VARCHAR(1000) instead of the default 255
    private String bio;

    @ManyToMany(mappedBy = "authors")
    // mappedBy = "authors" means Book is the OWNING side of this relationship
    // (Book's @JoinTable will define the actual join table in the DB)
    // Author is just the INVERSE side - it doesn't control the foreign keys
    private Set<Book> books = new HashSet<>();
}