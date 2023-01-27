package com.ShopIT.Service;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.PaymentReturnResponse;
import com.razorpay.RazorpayException;
import org.springframework.http.ResponseEntity;
import java.security.SignatureException;
public interface PaymentService {
    ResponseEntity<?> createOrder(User user, long amt) throws RazorpayException;
    ResponseEntity<?> updateOrder(User user, PaymentReturnResponse response) throws SignatureException;
}
