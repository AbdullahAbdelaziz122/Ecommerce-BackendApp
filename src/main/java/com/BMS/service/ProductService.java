package com.BMS.service;


import com.BMS.model.Category;
import com.BMS.model.Product;
import com.BMS.model.Tag;
import com.BMS.payloads.ProductDTO;
import com.BMS.payloads.ProductResponse;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;


public interface ProductService {
    // Create
    ProductDTO addProduct(Long categoryID,List<String> tags, Product product);

    // Read
    public ProductResponse searchProducts(Integer pageNumber, Integer pageLimit,String name, String brand, String category, List<String> tags);

    // Update
    public ProductDTO updateProduct(Product product, Long productId);
    // Delete

}
