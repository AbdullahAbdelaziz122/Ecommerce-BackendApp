package com.BMS.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private List<OrderDTO> data;
    private Integer pageNumber;
    private Integer pageLimit;
    private Long totalElements;
    private Integer totalPages;

}
