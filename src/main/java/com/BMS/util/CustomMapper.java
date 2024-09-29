package com.BMS.util;

import com.BMS.model.Cart;
import com.BMS.model.CartItem;
import com.BMS.model.Order;
import com.BMS.model.OrderItem;
import com.BMS.payloads.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class CustomMapper {

    private final ModelMapper modelMapper; // Initialize via constructor injection

    // Remove the mapper field as it is unnecessary
    // private CustomMapper mapper;

    public OrderItem cartItemToOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setDiscount(cartItem.getDiscount());
        orderItem.setOrderedProductPrice(cartItem.getProductPrice());
        orderItem.setOrder(order);
        return orderItem;
    }

    public List<OrderItem> cartItemsToOrderItems(List<CartItem> cartItems, Order order) {
        return cartItems
                .stream()
                .map(cartItem -> cartItemToOrderItem(cartItem, order))
                .toList();
    }

    public CartItemDTO cartItemToCartItemDTO(CartItem cartItem, CartDTO cartDTO) {
        CartItemDTO cartItemDTO = new CartItemDTO();

        cartItemDTO.setCartItemId(cartItem.getCartItemId());
        cartItemDTO.setCart(cartDTO);
        cartItemDTO.setDiscount(cartItem.getDiscount());
        cartItemDTO.setProduct(modelMapper.map(cartItem.getProduct(), ProductDTO.class));
        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setProductPrice(cartItem.getProductPrice());

        return cartItemDTO;
    }

    public List<CartItemDTO> cartItemsToCartItemDTOS(List<CartItem> cartItems, CartDTO cartDTO) {
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();

        cartItems.forEach(cartItem -> {
            cartItemDTOS.add(cartItemToCartItemDTO(cartItem, cartDTO));
        });

        return cartItemDTOS;
    }

    public CartDTO cartToCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getCartId());

        List<CartItem> cartItems = cart.getCartItems();
        cartDTO.setCartItems(this.cartItemsToCartItemDTOS(cartItems, cartDTO)); // Use 'this' instead of 'mapper'

        cartDTO.setTotalPrice(cart.getTotalPrice());

        return cartDTO;
    }

    public List<CartDTO> cartsToCartDTOS(List<Cart> carts) {
        return carts.stream()
                .map(this::cartToCartDTO)
                .collect(Collectors.toList());
    }

    public OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem){
        OrderItemDTO orderItemDTO = new OrderItemDTO();

        orderItemDTO.setOrderItemId(orderItem.getOrderItemId());
        orderItemDTO.setProduct(modelMapper.map(orderItem.getProduct(), ProductDTO.class));
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setDiscount(orderItem.getDiscount());
        orderItemDTO.setOrderProductPrice(orderItem.getOrderedProductPrice());

        return orderItemDTO;
    }

    public List<OrderItemDTO> orderItemsToOrderItemDTOS(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::orderItemToOrderItemDTO)
                .collect(Collectors.toList());
    }



    public OrderDTO orderToOrderDTO(Order order){

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setSecretId(order.getSecretId());
        orderDTO.setName(order.getName());
        orderDTO.setEmail(order.getEmail());
        orderDTO.setAddress(order.getAddress());
        orderDTO.setPhoneNo(order.getPhoneNo());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setOrderItems(this.orderItemsToOrderItemDTOS(order.getOrderItemsList()));
        orderDTO.setOrderStatus(order.getOrderStatus());
        return orderDTO;
    }

    public List<OrderDTO> ordersToOrderDTOS(List<Order> orders){
        return orders.stream()
                .map(this:: orderToOrderDTO)
                .collect(Collectors.toList());
    }



}
