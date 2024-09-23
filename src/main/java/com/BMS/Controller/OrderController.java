package com.BMS.Controller;

import com.BMS.model.Order;
import com.BMS.payloads.OrderDTO;
import com.BMS.payloads.OrderRequestDTO;
import com.BMS.payloads.OrderResponse;
import com.BMS.service.OrderRequestService;
import com.BMS.service.OrderService;
import com.BMS.util.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final OrderRequestService orderRequestService;


    @PostMapping("/public/order/create")
    public ResponseEntity<OrderDTO> placeOrder(
            HttpServletRequest request,
            HttpServletResponse response,
            @Valid @RequestParam(value = "name", required = true) String name,
            @Valid @RequestParam(value = "email", required = true) String email,
            @Valid @RequestParam(value = "phone", required = true) String phone,
            @Valid @RequestParam(value = "address", required = true) String address
    )
    {
        return new ResponseEntity<>(orderService.placeOrder(request, response, name, email, phone, address), HttpStatus.CREATED);
    }


    @GetMapping("/admin/order/search")
    public ResponseEntity<OrderResponse> searchOrders(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageLimit", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageLimit,
            @RequestParam(value = "orderId", required = false) Long orderId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "status", required = false) String status){

        return new ResponseEntity<>(orderService.SearchOrders(pageNumber, pageLimit, orderId,name, email, phone, address, status), HttpStatus.OK);
    }

    @GetMapping("/public/order/search")
    public ResponseEntity<OrderDTO> getCustomerOrder(@RequestParam(value = "secretId") String secretId){
        return new ResponseEntity<>(orderService.getCustomerOder(secretId), HttpStatus.OK);
    }


    @PutMapping("/admin/order/update")
    public ResponseEntity<OrderDTO> updateOrder(
            @RequestBody Order order,
            @RequestParam(value = "orderId", required = true) Long orderId){

        return new ResponseEntity<>(orderService.updateOrder(order, orderId), HttpStatus.OK);
    }

    @DeleteMapping("/admin/order/delete")
    public ResponseEntity<String> deleteOrder(
            @RequestParam(value = "orderId", required = true) Long orderId
    ){
        return new ResponseEntity<>(orderService.deleteOrder(orderId), HttpStatus.OK);
    }



    // for users to place requests
    @PostMapping("/public/order/request")
    public ResponseEntity<OrderRequestDTO> placeOrderRequest(
            @RequestParam(value = "secretId") String secretId,
            @RequestBody String requestDescription){
        return new ResponseEntity<>(orderRequestService.PlaceOrderRequest(secretId, requestDescription), HttpStatus.CREATED);
    }



}
