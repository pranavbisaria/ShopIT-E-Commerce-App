package com.ShopIT.Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReturnResponse {
    private String razorpay_payment_id;
    private String razorpay_order_id;
    private String razorpay_signature;
}
