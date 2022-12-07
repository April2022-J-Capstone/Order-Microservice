package com.smoothstack.ordermicroservice.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewOrderItem {
    
    private Integer menuItemId;

    private String notes;

    private String name;

    private Double discount;

    private Double price;

    private List<Integer> discountIds;
}
