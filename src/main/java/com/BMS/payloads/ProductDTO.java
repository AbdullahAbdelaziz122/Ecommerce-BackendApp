package com.BMS.payloads;

import com.BMS.model.Category;
import com.BMS.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String brand;
    private String image;
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
    private List<Tag> tags;

}
