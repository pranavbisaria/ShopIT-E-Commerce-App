package com.ShopIT.Service.Impl;
import com.ShopIT.Config.AppConstants;
import com.ShopIT.Config.PaymentConfigs;
import com.ShopIT.Exceptions.ResourceNotFoundException;
import com.ShopIT.Models.*;
import com.ShopIT.Payloads.*;
import com.ShopIT.Repository.*;
import com.ShopIT.Service.PaymentService;
import com.ShopIT.Service.ProductService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import static org.springframework.http.HttpStatus.OK;
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final MyOrdersRepo myOrdersRepo;
    private final PaymentConfigs paymentConfigs;
    private final ProfileRepo profileRepo;
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final ProductInCartRepo productInCartRepo;
    private final MerchantProfileRepo merchantProfileRepo;
    @Override
    @Transactional
    public ResponseEntity<?> createOrder(Profile profile, long amt, Address address) throws RazorpayException {
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
            myOrders.setProfiles(profile);
            myOrders.setAddress(address);
            this.myOrdersRepo.save(myOrders);
            return new ResponseEntity<>(order.toString(), OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    @Override
    public ResponseEntity<?> updateOrder(User user, Profile profile, PaymentReturnResponse response) throws SignatureException {
        MyOrders myOrders = this.myOrdersRepo.findByOrderId(response.getRazorpay_order_id()).orElseThrow(()-> new ResourceNotFoundException("Order", "OrderId: "+response.getRazorpay_order_id(),0));
        if(!response.getRazorpay_signature().equals(calculateRFC2104HMAC(response.getRazorpay_order_id()+"|"+response.getRazorpay_payment_id(), paymentConfigs.getKeySecret())) && !(Objects.equals(profile.getId(), myOrders.getProfiles().getId()))){
            return new ResponseEntity<>(new ApiResponse("Invalid Payment Entry!!!", false), HttpStatus.PAYMENT_REQUIRED);
        }
        myOrders.setPaymentId(response.getRazorpay_payment_id());
        myOrders.setStatus("paid");
        List<ProductInCart> productInCarts = this.productInCartRepo.findByUser(profile.getUser());
        productInCarts.forEach((product)->{
                Product product1 = product.getProduct();
                User userProvider = product1.getProvider();
                MerchantProfile merchantProfile = this.merchantProfileRepo.findByUser(userProvider);
                MerchantOrderReceived merchantOrder = new MerchantOrderReceived();
                merchantOrder.setAddress(myOrders.getAddress());
                merchantOrder.setPaymentId(myOrders.getPaymentId());
                merchantOrder.setProduct(product1);
                merchantOrder.setQuantity(product.getNoOfProducts());
                merchantOrder.setDateOfOrder(product.getDateOfOrder());
                merchantOrder.setAmountReceived((int) (product1.getOriginalPrice()*(100-product1.getOfferPercentage())*product.getNoOfProducts()));
                merchantProfile.getPaymentsReceived().add(merchantOrder);
                this.productService.emptyMyCart(profile.getUser());
                this.merchantProfileRepo.save(merchantProfile);
        });
        this.myOrdersRepo.saveAndFlush(myOrders);
        return new ResponseEntity<>(this.modelMapper.map(myOrders, OrderDto.class), OK);
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
//Get All my Orders
    @Override
    public PageResponse getMyOrders(User user, PageableDto pageable){
        Profile profile = this.profileRepo.findByUser(user);
        Integer pN = pageable.getPageNumber(), pS = pageable.getPageSize();
        Sort sort = null;
        if(pageable.getSortDir().equalsIgnoreCase("asc")){
            sort = Sort.by(pageable.getSortBy()).ascending();
        }
        else{
            sort = Sort.by(pageable.getSortBy()).descending();
        }
        Pageable p = PageRequest.of(pN, pS, sort);
        Page<MyOrders> pageReview = this.myOrdersRepo.findByProfiles(profile, p);
        List<MyOrders> productReview = pageReview.getContent();
        List<OrderDto> reviewDTOs = new ArrayList<>();
        productReview.forEach((userOrder)-> {
            OrderDto ordersDto = this.modelMapper.map(userOrder, OrderDto.class);
            reviewDTOs.add(ordersDto);
        });
        return new PageResponse(new ArrayList<>(reviewDTOs), pageReview.getNumber(), pageReview.getSize(), pageReview.getTotalPages(), pageReview.getTotalElements(), pageReview.isLast());
    }
//    @Override
//    public ResponseEntity<?> getProviderOrders(User user, PageableDto pageable){
//        MerchantProfile merchantProfile = this.merchantProfileRepo.findByUser(user);
//        return new ResponseEntity<>()
//    }
}
