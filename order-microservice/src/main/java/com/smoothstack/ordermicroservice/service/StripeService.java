package com.smoothstack.ordermicroservice.service;

import com.smoothstack.common.repositories.DiscountRepository;
import com.smoothstack.ordermicroservice.data.CouponDTO;
import com.smoothstack.ordermicroservice.data.NewOrder;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.TaxRate;
import com.stripe.model.checkout.Session;
import com.stripe.param.CouponCreateParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.TaxRateCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StripeService {

    @Value("${stripe.publishable.key}")
    private String API_KEY;

    @Value("${front.end.domain}")
    private String DOMAIN;

    @Autowired
    private DiscountService discountService;

    public String getPaymentPage(NewOrder order) throws StripeException {
        Stripe.apiKey = API_KEY;
        String YOUR_DOMAIN = DOMAIN;
        SessionCreateParams params = null;

        List<SessionCreateParams.LineItem> lineItems = order.getItems().stream().map(i -> {
            return SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(i.getName())
                                                    .build()
                                    ).setCurrency("usd")
                                    .setUnitAmount((long) (i.getPrice() * 100.00))
                                    .setTaxBehavior(SessionCreateParams.LineItem.PriceData.TaxBehavior.EXCLUSIVE)
                                    .build())
                    .build();
        }).collect(Collectors.toList());

        if(order.getCouponId() != null) {
            params = SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl(YOUR_DOMAIN + "/checkout/success-order-confirmation")
                            .setCancelUrl(YOUR_DOMAIN + "/checkout/fail-order-confirmation")
                            .addAllLineItem(lineItems)
                            .addDiscount(
                                    SessionCreateParams.Discount.builder().setCoupon(order.getCouponId()).build()
                            )
                            .setCustomer(Customer.create(
                                                CustomerCreateParams.builder()
                                                        .setShipping(CustomerCreateParams.Shipping.builder()
                                                                .setName("home")
                                                                .setAddress(CustomerCreateParams.Shipping.Address.builder()
                                                                        .setCountry("US")
                                                                        .setCity(order.getShippingAddress().getCity())
                                                                        .setState(order.getShippingAddress().getState())
                                                                        .setPostalCode(order.getShippingAddress().getZipCode().toString())
                                                                        .setLine1(order.getShippingAddress().getAddress())
                                                                        .build()
                                                                )
                                                                .build()
                                                        )
                                                        .build()
                                            )
                                    .getId()
                            )
                            .setCustomerUpdate(
                                    SessionCreateParams.CustomerUpdate.builder()
                                            .setShipping(SessionCreateParams.CustomerUpdate.Shipping.AUTO)
                                            .setAddress(SessionCreateParams.CustomerUpdate.Address.AUTO)
                                            .build())
                            .setShippingAddressCollection(
                                    SessionCreateParams.ShippingAddressCollection.builder()
                                            .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.US)
                                            .build())
                            .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
                            .build();
        } else {
            params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(YOUR_DOMAIN + "/checkout/success-order-confirmation")
                    .setCancelUrl(YOUR_DOMAIN + "/checkout/fail-order-confirmation")
                    .addAllLineItem(lineItems)
                    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
                    .setCustomer(Customer.create(
                                            CustomerCreateParams.builder()
                                                    .setShipping(CustomerCreateParams.Shipping.builder()
                                                            .setName(order.getShippingAddress().getLocationName())
                                                            .setAddress(CustomerCreateParams.Shipping.Address.builder()
                                                                    .setCountry("US")
                                                                    .setCity(order.getShippingAddress().getCity())
                                                                    .setState(order.getShippingAddress().getState())
                                                                    .setPostalCode(order.getShippingAddress().getZipCode().toString())
                                                                    .setLine1(order.getShippingAddress().getAddress())
                                                                    .build()
                                                            )
                                                            .build()
                                                    )
                                                    .build()
                                    )
                                    .getId()
                    )
                    .setCustomerUpdate(
                            SessionCreateParams.CustomerUpdate.builder()
                                    .setShipping(SessionCreateParams.CustomerUpdate.Shipping.AUTO)
                                    .setAddress(SessionCreateParams.CustomerUpdate.Address.AUTO)
                                    .build())
                    .setShippingAddressCollection(
                            SessionCreateParams.ShippingAddressCollection.builder()
                                    .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.US)
                                    .build())
                    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
                    .build();
        }



        Session session = Session.create(params);

        return session.toJson();
    }

    public String createCoupon(CouponDTO couponDTO) throws StripeException {
        Stripe.apiKey = API_KEY;
        CouponCreateParams params = null;
        CouponCreateParams.Duration duration = CouponCreateParams.Duration.ONCE;

        switch(couponDTO.getDuration()) {
            case 0:
                duration = CouponCreateParams.Duration.ONCE;
                break;
            case 1:
                duration = CouponCreateParams.Duration.FOREVER;
                break;
            case 2:
                duration = CouponCreateParams.Duration.REPEATING;
                break;
        }

        // either couponDTO is a flat amount off or a percent off
        if(couponDTO.getAmount() != null) {
            // default couponDTO duration

            params = CouponCreateParams.builder()
                    .setId(couponDTO.getId())
                    .setName(couponDTO.getName())
                    .setAmountOff(couponDTO.getAmount())
                    .setDuration(duration)
                    .setMaxRedemptions(couponDTO.getMaxRedemptions())
                    .setCurrency(couponDTO.getCurrency())
                    .setRedeemBy(couponDTO.getRedeemByTimeStamp())
                    .build();
        } else {
            params = CouponCreateParams.builder()
                    .setId(couponDTO.getId())
                    .setName(couponDTO.getName())
                    .setPercentOff(couponDTO.getPercent())
                    .setDuration(duration)
                    .setMaxRedemptions(couponDTO.getMaxRedemptions())
                    .setCurrency(couponDTO.getCurrency())
                    .setRedeemBy(couponDTO.getRedeemByTimeStamp())
                    .build();
        }

        Coupon coupon = Coupon.create(params);

        return coupon.toJson();
    }

    public String findCouponById(String couponId) throws StripeException {
        Stripe.apiKey = API_KEY;
        Coupon coupon = Coupon.retrieve(couponId);
        return coupon.toJson();
    }

    public Boolean couponExistsById(String couponId) throws StripeException {
        try {
            Stripe.apiKey = API_KEY;
            Coupon coupon = Coupon.retrieve(couponId);
            return true;
        } catch (StripeException e) {
            return false;
        }

    }

}
