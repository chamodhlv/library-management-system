package com.chamodh.library_management_system.repository;

import com.chamodh.library_management_system.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String title);
    // "Containing" = SQL LIKE %title% (partial match)
    // "IgnoreCase" = case-insensitive search
    // Useful for a search bar - user types "harry" and finds "Harry Potter"

    List<Book> findByAvailableCopiesGreaterThan(int copies);
    // Handy later for "show only books currently available to borrow"
}