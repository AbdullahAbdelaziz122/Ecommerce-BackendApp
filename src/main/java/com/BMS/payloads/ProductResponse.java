package com.BMS.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {


    private List<ProductDTO> data;
    private Integer pageNumber;
    private Integer pageLimit;
    private Long totalElements;
    private Integer totalPages;

}
