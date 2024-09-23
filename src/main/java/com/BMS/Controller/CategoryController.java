package com.BMS.Controller;

import com.BMS.util.AppConstants;
import com.BMS.model.Category;
import com.BMS.payloads.CategoryDTO;
import com.BMS.payloads.CategoryResponse;
import com.BMS.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;

    @PostMapping("/admin/category/create")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category){



        CategoryDTO savedCategory = categoryService.createCategory(category);

        return new ResponseEntity<CategoryDTO>(savedCategory, HttpStatus.CREATED);
    }


    @GetMapping("/admin/category/search")
    public ResponseEntity<CategoryResponse> getCategories(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageLimit",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageLimit,
            @RequestParam(value = "categoryName", required = false) String categoryName
    ){
       CategoryResponse categoryResponse = categoryService.getCategories(pageNumber, pageLimit, categoryName);

       return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }





    @PutMapping("/admin/category/update")
    public ResponseEntity<String> updateCategory(
            @RequestBody Category category,
            @RequestParam(value = "categoryId", required = true) Long categoryId){

        String response = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/admin/category/delete")
    public ResponseEntity<String> deleteCategory(@RequestParam(value = "categoryId", required = true) Long categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category Has been deleted", HttpStatus.OK);

    }


}
