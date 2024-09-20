package com.BMS.repository;

import com.BMS.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findCategoryByName(String categoryName);
    Optional<Category> findCategoryByNameLikeIgnoreCase(String categoryName);
    Page<Category> findCategoryByNameLikeIgnoreCase(String categoryName, Pageable pageable);

}
