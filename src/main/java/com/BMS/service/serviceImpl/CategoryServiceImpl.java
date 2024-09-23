package com.BMS.service.serviceImpl;

import com.BMS.exceptions.ResourceAlreadyExist;
import com.BMS.exceptions.ResourceNotFoundException;
import com.BMS.model.Category;
import com.BMS.model.Product;
import com.BMS.payloads.CategoryDTO;
import com.BMS.payloads.CategoryResponse;
import com.BMS.repository.CategoryRepository;
import com.BMS.repository.ProductRepository;
import com.BMS.service.CategoryService;
import com.BMS.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    @Override
    public CategoryDTO createCategory(Category category) {
        boolean isCategoryExists = categoryRepository.findCategoryByNameLikeIgnoreCase(category.getName()).isPresent();
        if(isCategoryExists){
           throw new ResourceAlreadyExist("Category with the name: "+ category.getName()+" already exists !!");
        }

        Category savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDTO.class);
    }




    @Override
    public CategoryResponse getCategories(Integer pageNumber, Integer pageLimit, String categoryName) {

        Pageable pageable = PageRequest.of(pageNumber, pageLimit, Sort.unsorted());
        Page<Category> categoryPage;

        if (categoryName != null && !categoryName.isEmpty()) {
            categoryPage = categoryRepository.findCategoryByNameLikeIgnoreCase(categoryName, pageable);
        } else {
            categoryPage = categoryRepository.findAll(pageable);
        }

        List<Category> categories = categoryPage.getContent();

        if (categories.isEmpty()) {
            throw new RuntimeException("No category is created till now");
        }

        List<CategoryDTO> categoryDTOS = categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setData(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageLimit(categoryPage.getSize());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());

        return categoryResponse;
    }

    @Override
    public CategoryDTO updateCategory(Category category, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("There are no category with Id: "+ categoryId));
        category.setId(categoryId);
        savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }


    @Override
    public String deleteCategory(Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("There are no category with Id: "+ categoryId));
        categoryRepository.deleteById(categoryId);
        List<Product> products = savedCategory.getProducts();

        products.forEach(product -> {
            productService.deleteProduct(product.getId());
        });
        return "Category has been deleted successfully";

    }


}
