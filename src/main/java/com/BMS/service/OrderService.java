package com.BMS.service;

import com.BMS.model.Order;
import com.BMS.payloads.OrderDTO;
import com.BMS.payloads.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface OrderService {
    OrderDTO placeOrder(HttpServletRequest request, HttpServletResponse response, String name, String email, String phone, String address);

    OrderResponse SearchOrders(Integer pageNumber, Integer pageLimit, Long orderId, String name, String email, String phone, String address, String status );

    OrderDTO getCustomerOder(String secretId);


    OrderDTO updateOrder(Order order, Long orderId);

    String deleteOrder(Long orderId);
}
