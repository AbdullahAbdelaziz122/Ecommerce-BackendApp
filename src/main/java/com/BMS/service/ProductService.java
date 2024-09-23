package com.BMS.service;


import com.BMS.model.Product;
import com.BMS.payloads.ProductDTO;
import com.BMS.payloads.ProductResponse;

import java.util.List;


public interface ProductService {
    // Create
    ProductDTO addProduct(Long categoryID,List<String> tags, Product product);

    // Read
    ProductResponse searchProducts(Integer pageNumber, Integer pageLimit,String name, String brand, String category, List<String> tags);

    // Update
    ProductDTO updateProduct(Product product, Long productId);
    // Delete
    void deleteProduct(Long productId);
}
