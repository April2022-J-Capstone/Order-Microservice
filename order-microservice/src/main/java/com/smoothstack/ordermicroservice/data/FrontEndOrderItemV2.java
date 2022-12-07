package com.smoothstack.ordermicroservice.data;

import com.smoothstack.common.models.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FrontEndOrderItemV2 {

    private Integer id;

    private Integer restaurantId;

    private String name;

    private String description;

    private String notes;

    private Double discount;

    private Double price;

    private Boolean enabled;

    public static FrontEndOrderItemV2 getFrontEndOrderItem(OrderItem o) {
        return builder()
                .id(o.getId())
                .restaurantId(o.getMenuItems().getRestaurants().getId())
                .name(o.getMenuItems().getName())
                .description(o.getMenuItems().getDescription())
                .notes(o.getNotes())
                .discount(o.getDiscount())
                .price(o.getPrice())
                .enabled(o.isEnabled())
                .build();
    }

}