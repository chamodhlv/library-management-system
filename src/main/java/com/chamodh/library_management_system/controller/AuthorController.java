package com.chamodh.library_management_system.controller;

import com.chamodh.library_management_system.dto.AuthorRequestDto;
import com.chamodh.library_management_system.dto.AuthorResponseDto;
import com.chamodh.library_management_system.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// @RestController = @Controller + @ResponseBody combined -
// every method's return value gets automatically converted to JSON
@RequestMapping("/api/authors")
// Base path - every endpoint below is prefixed with /api/authors
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorResponseDto> createAuthor(@Valid @RequestBody AuthorRequestDto requestDto) {
        // @Valid triggers the @NotBlank etc. validation on AuthorRequestDto -
        // if validation fails, Spring throws MethodArgumentNotValidException automatically
        // (we'll handle that exception type in GlobalExceptionHandler shortly)
        AuthorResponseDto created = authorService.createAuthor(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
        // 201 Created - correct status code for a successful POST that creates a resource
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> getAuthorById(@PathVariable Long id) {
        AuthorResponseDto author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
        // 200 OK - shorthand for new ResponseEntity<>(author, HttpStatus.OK)
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> getAllAuthors() {
        List<AuthorResponseDto> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequestDto requestDto) {
        AuthorResponseDto updated = authorService.updateAuthor(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
        // 204 No Content - correct status for a successful DELETE with no response body
    }
}