package com.BMS.repository;

import com.BMS.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartId = ?1 AND ci.product.id = ?2")
    Optional<CartItem> findCartItemByProductIdAndCartId(Long cartId, Long productId);

    Optional<List<CartItem>> findCartItemsByProductId(Long productId);



    void deleteAllByCart_CartId(Long cartId);


//    void deleteCartItemById(Long cartItemId);

}


