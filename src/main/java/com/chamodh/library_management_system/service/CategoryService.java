package com.chamodh.library_management_system.service;

import com.chamodh.library_management_system.dto.CategoryRequestDto;
import com.chamodh.library_management_system.dto.CategoryResponseDto;
import com.chamodh.library_management_system.entity.Category;
import com.chamodh.library_management_system.exception.ResourceNotFoundException;
import com.chamodh.library_management_system.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        Category category = new Category();
        category.setName(requestDto.getName());

        Category saved = categoryRepository.save(category);
        return mapToResponseDto(saved);
    }

    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToResponseDto(category);
    }

    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setName(requestDto.getName());

        Category updated = categoryRepository.save(category);
        return mapToResponseDto(updated);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    private CategoryResponseDto mapToResponseDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName());
    }
}