package com.BMS.specification;

import com.BMS.model.Product;
import com.BMS.model.Tag;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    // filter by product name (case - in-sensitive)
    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }


    // filter by product brand
    public static Specification<Product> hasBrand(String brand){
        return (root, query, criteriaBuilder) -> {
            if(brand == null || brand.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("brand"), brand);
        };
    }

    // filter by category name
    public static Specification<Product> hasCategory(String categoryName){
        return (root, query, criteriaBuilder) -> {
            if(categoryName == null|| categoryName.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("category").get("name"), categoryName);
        };
    }


    // filter by multiple tags

    public static Specification<Product> hasTags(List<String> tagNames){
    return (root, query, criteriaBuilder) -> {
        if(tagNames == null || tagNames.isEmpty()){
            return criteriaBuilder.conjunction();
        }

        Join<Product, Tag> tagJoin = root.join("tags");
        return tagJoin.get("name").in(tagNames);
    };
}
}
