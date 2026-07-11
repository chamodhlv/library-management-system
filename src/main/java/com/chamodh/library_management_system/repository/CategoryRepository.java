package com.chamodh.library_management_system.repository;

import com.chamodh.library_management_system.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    // Spring Data JPA reads this method name and auto-generates:
    // SELECT * FROM categories WHERE name = ?
    // Optional<> because a category with that name might not exist
}