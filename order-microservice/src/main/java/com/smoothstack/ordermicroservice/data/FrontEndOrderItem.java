package com.smoothstack.ordermicroservice.data;

import com.smoothstack.common.models.Order;
import com.smoothstack.common.models.OrderItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.boot.actuate.integration.IntegrationGraphEndpoint;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FrontEndOrderItem {
    
    private Integer id;

    private Integer restaurantId;

    private String restaurantName;

    private String name;

    private String description;

    private String notes;

    private Double discount;

    private Double price;

    private Boolean enabled;
}
