package com.smoothstack.ordermicroservice.data;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CouponDTO {
    private String id;
    private String name;
    private Long amount;
    private BigDecimal percent;
    private Integer duration;
    private String currency;
    private Long maxRedemptions;
    private Long redeemByTimeStamp;
}
