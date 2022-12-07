package com.smoothstack.ordermicroservice.service;

import com.smoothstack.common.models.Discount;
import com.smoothstack.common.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    @Autowired
    DiscountRepository discountRepository;

    public Discount findDiscountByName(String discountName) {
        try {
            Discount discount = discountRepository.findByName(discountName);
            return discount;
        } catch (Exception e) {
            throw e;
        }
    }

    public Discount saveDiscount(Discount discount) {
        try {
            if(discount.getId() != null) {
                throw new RuntimeException("To save a new Discount, there should be no associated id");
            }

            if(discount.getDiscountEnd() != null && discount.getDiscountStart().isAfter(discount.getDiscountEnd())) {
                throw new RuntimeException("Discount End Date should not be before the start date");
            }

            if(discount.getDiscountEnd() != null && discount.getDiscountStart().isEqual(discount.getDiscountEnd())) {
                throw new RuntimeException("Discount End Date should not be the same as the start date");
            }

            if(discount.getPercent() != null && discount.getAmount() != null) {
                throw new RuntimeException("Either use percent or amount off of order not both");
            }

            if(discount.getAmount() != null && discount.getAmount() <= 0) {
                throw new RuntimeException("Amount off of order should be less than or equal to zero");
            }

            if(discount.getPercent() != null && (discount.getPercent() <= 0)) {
                throw new RuntimeException("Percent off should not be less than or equal to zero");
            }

            //make sure there is a unique name

            //make sure now is after between discount times

            return discountRepository.saveAndFlush(discount);
        } catch (Exception e) {
            throw e;
        }
    }
}
