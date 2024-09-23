package com.BMS.service;

import com.BMS.payloads.OrderRequestDTO;

public interface OrderRequestService
{
    OrderRequestDTO PlaceOrderRequest(String secretId, String requestDescription);
//    public OrderDTO RequestUpdateOrder(Order order, String secretId){
//        Order existingOrder = orderRepository.findBySecretId(secretId)
//                .orElseThrow(()-> new ResourceNotFoundException("No order with ID: "+secretId+" Were found"));
//
//        BeanUtils.copyProperties(order, existingOrder, getNullPropertyNames(order));
//
//
//
//        return mapper.orderToOrderDTO(updatedOrder);
//    }
//
//    public String RequestDeleteOrder(Long orderId){
//        Order existingOrder = orderRepository.findById(orderId)
//                .orElseThrow(()-> new ResourceNotFoundException("No order with ID: "+orderId+" Were found"));
//
//        orderRepository.deleteById(orderId);
//
//        return "Order has been deleted successfully";
//    }
}
