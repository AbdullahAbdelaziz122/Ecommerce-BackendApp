package com.BMS.repository;

import com.BMS.model.Product;
import com.BMS.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {


    List<Product> findByNameContainingIgnoreCase(String name);
    Page<List<Product>> findByNameLike(String name, Pageable pageable);
    Page<List<Product>> findByNameLikeAndCategory_Name(String name, String Category_Name, Pageable pageable);
    Page<List<Product>> findByNameLikeAndCategory_NameAndTags(String name, String Category_Name, List<Tag> tags, Pageable pageable);



}

