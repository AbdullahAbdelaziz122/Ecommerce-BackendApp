package com.BMS.repository;

import com.BMS.model.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Page<Cart> findCartByCartId(Long cartId, Pageable pageable);
}
