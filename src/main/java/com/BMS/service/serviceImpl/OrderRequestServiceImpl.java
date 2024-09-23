package com.BMS.service.serviceImpl;

import com.BMS.exceptions.ResourceNotFoundException;
import com.BMS.model.Order;
import com.BMS.model.OrderRequest;
import com.BMS.payloads.OrderRequestDTO;
import com.BMS.repository.OrderRepository;
import com.BMS.repository.OrderRequestRepository;
import com.BMS.service.OrderRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderRequestServiceImpl implements OrderRequestService {

    private final OrderRepository orderRepository;
    private final OrderRequestRepository orderRequestRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderRequestDTO PlaceOrderRequest(String secretId, String requestDescription){

        Order order = orderRepository.findBySecretId(secretId).orElseThrow(() -> new ResourceNotFoundException("No Order with Id: " + secretId));

        OrderRequest request = new OrderRequest();
        request.setOrder(order);
        request.setSecretId(secretId);
        request.setCreatedAt(new Date());

        request = orderRequestRepository.save(request);


        return modelMapper.map(request, OrderRequestDTO.class);
    }

}
