package com.smoothstack.ordermicroservice.controller;

import com.smoothstack.ordermicroservice.data.CouponDTO;
import com.smoothstack.ordermicroservice.data.NewOrder;
import com.smoothstack.ordermicroservice.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin
@RestController
@RequestMapping("stripe/")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("paymentPage")
    public ResponseEntity getPaymentPage(@RequestBody NewOrder order) {
        try {
            return ResponseEntity.accepted().body(stripeService.getPaymentPage(order));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("coupons/{couponId}")
    public ResponseEntity getCouponById(@PathVariable String couponId) {
        try {
            return ResponseEntity.ok().body(stripeService.findCouponById(couponId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("coupons/{couponId}/exists")
    public ResponseEntity getCouponExistsById(@PathVariable String couponId) {
        try {
            return ResponseEntity.ok().body(stripeService.couponExistsById(couponId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("coupons")
    public ResponseEntity postNewCoupon(@RequestBody CouponDTO coupon) {
        try {
            return ResponseEntity.accepted().body(stripeService.createCoupon(coupon));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
