package com.BMS.payloads;

import com.BMS.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagResponse {

    private List<Tag> data;
    private Integer pageNumber;
    private Integer pageLimit;
    private Long totalElements;
    private Integer totalPages;


}
