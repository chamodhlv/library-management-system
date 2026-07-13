package com.chamodh.library_management_system.service;

import com.chamodh.library_management_system.dto.*;
import com.chamodh.library_management_system.entity.Author;
import com.chamodh.library_management_system.entity.Book;
import com.chamodh.library_management_system.entity.Category;
import com.chamodh.library_management_system.repository.AuthorRepository;
import com.chamodh.library_management_system.repository.BookRepository;
import com.chamodh.library_management_system.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    // BookService needs all THREE repositories, because creating a book
    // involves looking up existing Authors and Categories by their IDs

    public BookResponseDto createBook(BookRequestDto requestDto) {
        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setIsbn(requestDto.getIsbn());
        book.setTotalCopies(requestDto.getTotalCopies());
        book.setAvailableCopies(requestDto.getTotalCopies());
        // KEY BUSINESS RULE: when a book is first created, all copies are
        // available - the client never sends availableCopies directly

        Set<Author> authors = resolveAuthors(requestDto.getAuthorIds());
        Set<Category> categories = resolveCategories(requestDto.getCategoryIds());
        book.setAuthors(authors);
        book.setCategories(categories);

        Book saved = bookRepository.save(book);
        return mapToResponseDto(saved);
    }

    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return mapToResponseDto(book);
    }

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public List<BookResponseDto> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public BookResponseDto updateBook(Long id, BookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        book.setTitle(requestDto.getTitle());
        book.setIsbn(requestDto.getIsbn());
        book.setTotalCopies(requestDto.getTotalCopies());
        // NOTE: we deliberately don't touch availableCopies here -
        // that field only changes through borrow/return operations,
        // not through a generic "update book details" call

        Set<Author> authors = resolveAuthors(requestDto.getAuthorIds());
        Set<Category> categories = resolveCategories(requestDto.getCategoryIds());
        book.setAuthors(authors);
        book.setCategories(categories);

        Book updated = bookRepository.save(book);
        return mapToResponseDto(updated);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    // Helper: takes a Set of author IDs from the request DTO,
    // fetches each real Author entity from the database, and collects them
    private Set<Author> resolveAuthors(Set<Long> authorIds) {
        return authorIds.stream()
                .map(authorId -> authorRepository.findById(authorId)
                        .orElseThrow(() -> new RuntimeException("Author not found with id: " + authorId)))
                .collect(Collectors.toSet());
    }

    private Set<Category> resolveCategories(Set<Long> categoryIds) {
        return categoryIds.stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId)))
                .collect(Collectors.toSet());
    }

    // Maps a full Book entity -> BookResponseDto, including nested
    // author/category summaries
    private BookResponseDto mapToResponseDto(Book book) {
        Set<AuthorSummaryDto> authorDtos = book.getAuthors().stream()
                .map(author -> new AuthorSummaryDto(author.getId(), author.getName()))
                .collect(Collectors.toSet());

        Set<CategorySummaryDto> categoryDtos = book.getCategories().stream()
                .map(category -> new CategorySummaryDto(category.getId(), category.getName()))
                .collect(Collectors.toSet());

        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                authorDtos,
                categoryDtos
        );
    }
}