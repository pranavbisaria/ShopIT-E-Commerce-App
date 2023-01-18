package com.ShopIT.Service.Impl;

import com.ShopIT.Config.AppConstants;
import com.ShopIT.Config.PaymentConfigs;
import com.ShopIT.Exceptions.ResourceNotFoundException;
import com.ShopIT.Models.MyOrders;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.ApiResponse;
import com.ShopIT.Payloads.PaymentReturnResponse;
import com.ShopIT.Repository.MyOrdersRepo;
import com.ShopIT.Service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SignatureException;
import java.util.Objects;
import java.util.Random;

import static org.springframework.http.HttpStatus.OK;
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final MyOrdersRepo myOrdersRepo;
    private final PaymentConfigs paymentConfigs;
    @Override
    public ResponseEntity<?> createOrder(User user, long amt) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(paymentConfigs.getKeyId(), paymentConfigs.getKeySecret());
        Random rand = new Random();
        int receiptNo = rand.nextInt(899999) +100000;
        Order order = null;
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amt);
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
    @Override
    public ResponseEntity<?> updateOrder(User user, PaymentReturnResponse response) throws SignatureException {
        MyOrders myOrders = this.myOrdersRepo.findById(response.getRazorpay_order_id()).orElseThrow(()-> new ResourceNotFoundException("Order", "OrderId: "+response.getRazorpay_order_id(),0));
        if(!response.getRazorpay_signature().equals(calculateRFC2104HMAC(response.getRazorpay_order_id()+"|"+response.getRazorpay_payment_id(), paymentConfigs.getKeySecret())) && !(Objects.equals(user.getId(), myOrders.getUser().getId()))){
            return new ResponseEntity<>(new ApiResponse("Invalid Payment Entry!!!", false), HttpStatus.PAYMENT_REQUIRED);
        }
        myOrders.setPaymentId(response.getRazorpay_payment_id());
        myOrders.setStatus("paid");
        this.myOrdersRepo.saveAndFlush(myOrders);
        return new ResponseEntity<>(myOrders, OK);
    }
    public static String calculateRFC2104HMAC(String data, String secret) throws SignatureException {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), AppConstants.HMAC_SHA256_ALGORITHM);
            Mac mac = Mac.getInstance(AppConstants.HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = DatatypeConverter.printHexBinary(rawHmac).toLowerCase();
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
}
