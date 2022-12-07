package com.smoothstack.ordermicroservice.data;

import java.time.LocalDateTime;
import java.util.List;

import com.smoothstack.common.models.Location;
import com.stripe.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewOrder {
    
    private String restaurantNotes;

    private String driverNotes;

    private Double subTotal;

    private Double deliveryFee;

    private Double tax;

    private Double tip;

    private Double total;

    private Integer netLoyalty;

    private LocalDateTime timeCreated;

    private List<Integer> restaurantIds;

    private List<Integer> discountIds;

    private List<NewOrderItem> items;

    private String couponId;

    private Location shippingAddress;

}
