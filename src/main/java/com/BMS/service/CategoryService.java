package com.BMS.service;

import com.BMS.model.Category;
import com.BMS.payloads.CategoryDTO;
import com.BMS.payloads.CategoryResponse;

public interface CategoryService {

    // Create
    CategoryDTO createCategory(Category category);
    // Read
    CategoryResponse getCategories(Integer pageNumber, Integer pageLimit, String categoryName);
    // Update
    CategoryDTO updateCategory(Category category, Long categoryId);
    // Delete
    String deleteCategory(Long categoryId);
}
