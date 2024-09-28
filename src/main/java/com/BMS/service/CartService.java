package com.BMS.service;

import com.BMS.payloads.CartDTO;
import com.BMS.payloads.CartResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CartService {
    // create
    CartDTO addItemToCart(HttpServletRequest request, HttpServletResponse response, Long productId, Integer quantity);
    // read
    CartResponse getAllCarts(Integer pageNumber, Integer pageLimit, Long cartId);
    CartDTO getCustomerCart(HttpServletRequest request, HttpServletResponse response);
    // update
    CartDTO updateProductQuantityInCart(HttpServletRequest request, HttpServletResponse response, Long productId, Integer quantity);
    // delete
    void deleteProductFromCart(HttpServletRequest request, HttpServletResponse response, Long productId);

    void clearCart(Long cartId);

}
