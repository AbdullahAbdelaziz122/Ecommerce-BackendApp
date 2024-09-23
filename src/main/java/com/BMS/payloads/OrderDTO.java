package com.BMS.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String secretId;
    private String name;
    private String email;
    private String address;
    private String phoneNo;
    private Double totalAmount;
    private String orderStatus;
    private List<OrderItemDTO> orderItems = new ArrayList<>();

}
