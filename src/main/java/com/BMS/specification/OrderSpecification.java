package com.BMS.specification;

import com.BMS.model.Order;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

    // filter by ID
    public static Specification<Order> hasID(Long Id){
        return ((root, query, criteriaBuilder) -> {
            if(Id == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("Id"), Id);
        });
    }


    // filter by name
    public static Specification<Order> hasName(String name){
        return (root, query, criteriaBuilder) -> {
            if(name == null || name.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%"+ name.toLowerCase() +"%");
        };
    }


    //filter by email
    public static Specification<Order> hasEmail(String email){
        return (root, query, criteriaBuilder) -> {
            if(email == null || email.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),"%"+ email.toLowerCase() +"%");
        };
    }

    //filter by phone
    public static Specification<Order> hasPhone(String phone){
        return (root, query, criteriaBuilder) -> {
            if(phone == null || phone.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("phone"), phone);
        };
    }


    // filter by Address
    public static Specification<Order> hasAddress(String address){
        return (root, query, criteriaBuilder) -> {
            if(address == null || address.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("address")),"%"+ address.toLowerCase() +"%");
        };
    }

    // filter by Status
    public static Specification<Order> hasStatus(String status){
        return (root, query, criteriaBuilder) -> {
            if(status == null || status.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("status")),"%"+ status.toLowerCase() +"%");
        };
    }



}
