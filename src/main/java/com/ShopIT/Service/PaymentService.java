package com.ShopIT.Service;
import com.ShopIT.Models.Address;
import com.ShopIT.Models.Product;
import com.ShopIT.Models.Profile;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.PageResponse;
import com.ShopIT.Payloads.PageableDto;
import com.ShopIT.Payloads.PaymentReturnResponse;
import com.razorpay.RazorpayException;
import org.springframework.http.ResponseEntity;
import java.security.SignatureException;
public interface PaymentService {
    ResponseEntity<?> createOrder(Profile profile, long amt, Address address) throws RazorpayException;
    ResponseEntity<?> updateOrder(User user, Profile profile, PaymentReturnResponse response) throws SignatureException;

    ResponseEntity<?> updateDirectOrder(User user, Product product, Long n, Profile profile, PaymentReturnResponse response) throws SignatureException;

    //Get All my Orders
    PageResponse getMyOrders(User user, PageableDto pageable);

    PageResponse getAllMerchantOrder(User user, PageableDto pageable);
}
