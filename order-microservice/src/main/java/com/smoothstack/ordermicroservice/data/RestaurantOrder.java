package com.smoothstack.ordermicroservice.data;

import com.smoothstack.common.models.Order;
import com.smoothstack.common.models.OrderItem;
import com.smoothstack.common.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantOrder {

    private Integer id;

    private String orderStatus;

    private String restaurantNotes;

    private Double subTotal;

    private Double deliveryFee;

    private Double tax;

    private Double tip;

    private Double total;

    private LocalDateTime timeCreated;

    private LocalDateTime scheduledFor;

    private Integer netLoyalty;

    private Boolean enabled;

    private User driver;

    private List<OrderItem> orderItems;

    public static RestaurantOrder fromOrder(Order o) {
        return builder()
                .id(o.getId())
                .orderStatus(o.getOrderStatus())
                .restaurantNotes(o.getRestaurantNotes())
                .subTotal(o.getSubTotal())
                .deliveryFee(o.getDeliveryFee())
                .tax(o.getTax())
                .tip(o.getTip())
                .total(o.getTotal())
                .timeCreated(o.getTimeCreated())
                .scheduledFor(o.getScheduledFor())
                .netLoyalty(o.getNetLoyalty())
                .enabled(o.isEnabled())
                .driver(o.getDriver())
                .orderItems(o.getOrderItems())
                .build();
    }
}
