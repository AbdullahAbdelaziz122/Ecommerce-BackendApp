package com.BMS.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    private Long cartItemId;
    @JsonIgnore
    private CartDTO cart;
    private ProductDTO product;
    private Integer quantity;
    private double discount;
    private Double productPrice;

}
