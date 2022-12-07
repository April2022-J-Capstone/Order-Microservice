package com.smoothstack.ordermicroservice.controller;

import com.smoothstack.common.models.Discount;
import com.smoothstack.ordermicroservice.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DiscountController {

    @Autowired
    DiscountService discountService;

    @GetMapping("/discounts/discountName/{discountName}")
    public ResponseEntity getDiscountByName(@PathVariable String discountName) {
        try {
            return ResponseEntity.ok().body(discountService.findDiscountByName(discountName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/discounts")
    public ResponseEntity postDiscount(@RequestBody Discount discount) {
        try {
            return ResponseEntity.accepted().body(discountService.saveDiscount(discount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
