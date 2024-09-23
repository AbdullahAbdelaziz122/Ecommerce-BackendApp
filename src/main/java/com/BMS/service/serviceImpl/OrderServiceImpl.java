package com.BMS.service.serviceImpl;

import com.BMS.exceptions.ResourceNotFoundException;
import com.BMS.model.*;
import com.BMS.payloads.OrderDTO;
import com.BMS.payloads.OrderResponse;
import com.BMS.repository.OrderItemRepository;
import com.BMS.repository.OrderRepository;
import com.BMS.repository.ProductRepository;
import com.BMS.repository.ShoppingSessionRepository;
import com.BMS.service.CartService;
import com.BMS.service.OrderService;
import com.BMS.specification.OrderSpecification;
import com.BMS.util.BeanUtilsHelper;
import com.BMS.util.CustomMapper;
import com.BMS.util.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ShoppingSessionRepository shoppingSessionRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final CustomMapper mapper = new CustomMapper(modelMapper);
    private final SessionManager sessionManager;



    private String[] getNullPropertyNames(Order source) {
        return BeanUtilsHelper.getNullPropertyNames(source);
    }

    @Override
    public OrderDTO placeOrder(HttpServletRequest request, HttpServletResponse response,  String name, String email, String phone, String address){

        ShoppingSession session = sessionManager.manageSession(request, response);

        Cart cart = session.getCart();
        List<CartItem> cartItemList = cart.getCartItems();

        if(cartItemList.isEmpty()){
            throw new ResourceNotFoundException("Cart is Empty");
        }

        // create New Order
        Order order = new Order();

        order.setName(name);
        order.setSecretId(UUID.randomUUID().toString());
        order.setEmail(email);
        order.setPhoneNo(phone);
        order.setAddress(address);


        List<OrderItem> orderItems = mapper.cartItemsToOrderItems(cartItemList, order);
        order.setOrderItemsList(orderItems);

        for(OrderItem orderItem : orderItems){
            order.setTotalAmount(order.getTotalAmount() + (orderItem.getOrderedProductPrice() * orderItem.getQuantity()));
        }

        order.setOrderStatus("Pending");
        order = orderRepository.save(order);
        orderItems = orderItemRepository.saveAll(orderItems);
        cartItemList.forEach(cartItem -> {
            int quantity = cartItem.getQuantity();

            Product product = cartItem.getProduct();

            cartService.deleteProductFromCart(request,response, product.getId());

            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);
        });


        return mapper.orderToOrderDTO(order);

    }


    @Override
    public OrderResponse SearchOrders(Integer pageNumber, Integer pageLimit,Long orderId, String name, String email, String phone, String address, String status ){

        Pageable pageable = PageRequest.of(pageNumber, pageLimit);
        Page<Order> orderPage;


        // filtering process
        Specification<Order> spec = Specification
                .where(OrderSpecification.hasID(orderId))
                .and(OrderSpecification.hasName(name))
                .and(OrderSpecification.hasEmail(email))
                .and(OrderSpecification.hasPhone(phone))
                .and(OrderSpecification.hasAddress(address))
                .and(OrderSpecification.hasStatus(status));


        orderPage = orderRepository.findAll(spec, pageable);


        List<Order> orders = orderPage.getContent();
        List<OrderDTO> orderDTOS= mapper.ordersToOrderDTOS(orders);


        // Response construction
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setData(orderDTOS);
        orderResponse.setPageNumber(orderPage.getNumber());
        orderResponse.setPageLimit(orderPage.getSize());
        orderResponse.setTotalPages(orderPage.getTotalPages());
        orderResponse.setTotalElements(orderPage.getTotalElements());

        return orderResponse;

    }

    @Override
    public OrderDTO getCustomerOder(String secretId){
        return mapper
                    .orderToOrderDTO(orderRepository
                    .findBySecretId(secretId)
                    .orElseThrow(()-> new ResourceNotFoundException("No order with ID: "+secretId+" Were found")));
}


    @Override
    public OrderDTO updateOrder(Order order, Long orderId){
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("No order with ID: "+orderId+" Were found"));

        BeanUtils.copyProperties(order, existingOrder, getNullPropertyNames(order));

        Order updatedOrder = orderRepository.save(existingOrder);

        return mapper.orderToOrderDTO(updatedOrder);
    }

    @Override
    public String deleteOrder(Long orderId){
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("No order with ID: "+orderId+" Were found"));

        orderRepository.deleteById(orderId);

        return "Order has been deleted successfully";
    }




}
