package com.chamodh.library_management_system.service;

import com.chamodh.library_management_system.dto.AuthorRequestDto;
import com.chamodh.library_management_system.dto.AuthorResponseDto;
import com.chamodh.library_management_system.entity.Author;
import com.chamodh.library_management_system.exception.ResourceNotFoundException;
import com.chamodh.library_management_system.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorResponseDto createAuthor(AuthorRequestDto requestDto) {
        Author author = new Author();
        author.setName(requestDto.getName());
        author.setBio(requestDto.getBio());

        Author saved = authorRepository.save(author);
        return mapToResponseDto(saved);
    }

    public AuthorResponseDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return mapToResponseDto(author);
    }

    public List<AuthorResponseDto> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public AuthorResponseDto updateAuthor(Long id, AuthorRequestDto requestDto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));

        author.setName(requestDto.getName());
        author.setBio(requestDto.getBio());

        Author updated = authorRepository.save(author);
        return mapToResponseDto(updated);
    }

    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    private AuthorResponseDto mapToResponseDto(Author author) {
        return new AuthorResponseDto(author.getId(), author.getName(), author.getBio());
    }
}