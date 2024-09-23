package com.BMS.Controller;


import com.BMS.util.AppConstants;
import com.BMS.model.Product;
import com.BMS.payloads.ProductDTO;
import com.BMS.payloads.ProductResponse;
import com.BMS.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;


    // Create
    @PostMapping("/admin/product/create")
    public ResponseEntity<ProductDTO> addProduct(
            @Valid @RequestBody Product product,
            @RequestParam(value = "categoryId", required = true) Long categoryId,
            @RequestParam(value = "tags", required = false) List<String> tags
    )
    {
        return new ResponseEntity<ProductDTO>(productService.addProduct(categoryId, tags, product), HttpStatus.CREATED);
    }


    // Read

    @GetMapping("/public/product/search")
    public ResponseEntity<ProductResponse> searchProducts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageLimit", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageLimit,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "tags", required = false) List<String> tags
    )
    {
        return new ResponseEntity<>(productService.searchProducts(pageNumber, pageLimit, name, brand, category, tags), HttpStatus.OK);
    }


    @PutMapping("/admin/product/update")
    public ResponseEntity<ProductDTO> updateProduct(
            @RequestBody Product product,
            @RequestParam (value = "productId") Long productId
    )
    {
        return new ResponseEntity<>(productService.updateProduct(product, productId), HttpStatus.OK);
    }

    @DeleteMapping("/admin/product/delete")
    public ResponseEntity<String> deleteProduct(@RequestParam(value = "productId") Long productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Product has been successfully deleted", HttpStatus.OK);
    }
}
