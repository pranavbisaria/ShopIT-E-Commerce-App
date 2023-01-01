package com.ShopIT.Controllers;

import com.ShopIT.Config.AppConstants;
import com.ShopIT.Config.PaymentConfigs;
import com.ShopIT.Exceptions.ResourceNotFoundException;
import com.ShopIT.Models.MyOrders;
import com.ShopIT.Models.Product;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.PaymentReturnResponse;
import com.ShopIT.Repository.MyOrdersRepo;
import com.ShopIT.Repository.ProductRepo;
import com.ShopIT.Security.CurrentUser;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.razorpay.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SignatureException;
import java.util.Random;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final ProductRepo productRepo;
    private final MyOrdersRepo myOrdersRepo;
    private final PaymentConfigs paymentConfigs;

    @PostMapping("/createOrder/{productId}")
    public ResponseEntity<?> newPayment(@CurrentUser User user, @PathVariable("productId") Long productId) throws Exception {
        Product product = this.productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
        int amt = (int) (product.getOriginalPrice()*(100-product.getOfferPercentage()));
        RazorpayClient razorpay = new RazorpayClient(paymentConfigs.getKeyId(), paymentConfigs.getKeySecret());
        Random rand = new Random();
        int receiptNo = rand.nextInt(899999) +100000;
        Order order = null;
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amt); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_"+receiptNo);
            order = razorpay.orders.create(orderRequest);
            MyOrders myOrders = new MyOrders();
            myOrders.setOrderId(order.get("id"));
            myOrders.setAmount(order.get("amount"));
            myOrders.setCurrency(order.get("currency"));
            myOrders.setCreated_at(order.get("created_at"));
            myOrders.setStatus(order.get("status"));
            myOrders.setReceipt(order.get("receipt"));
            myOrders.setUser(user);
            System.out.println(order);
            this.myOrdersRepo.save(myOrders);
            return new ResponseEntity<>(order.toString(), OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
//    @PostMapping("/update_order")
//    public ResponseEntity<?> updateOrder(@RequestBody PaymentReturnResponse response){
//        MyOrders myOrders = this.myOrdersRepo.findById(response.getRazorpay_order_id()).orElseThrow(()-> new ResourceNotFoundException("Order", "OrderId: "+response.getRazorpay_order_id(),0));
//        if(){
//
//        }
//        if()
//        myOrders = response.getRazorpay_signature()
//        myOrders.setPaymentId(response.getRazorpay_payment_id());
//        myOrders.
//
//        return new ResponseEntity<>(, OK);
//    }

    public static String calculateRFC2104HMAC(String data, String secret) throws java.security.SignatureException {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), AppConstants.HMAC_SHA256_ALGORITHM);

            // get an hmac_sha256 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(AppConstants.HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());

            // base64-encode the hmac
            result = DatatypeConverter.printHexBinary(rawHmac).toLowerCase();

        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
}
