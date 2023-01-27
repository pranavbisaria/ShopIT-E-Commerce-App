package com.ShopIT.Controllers;
import com.ShopIT.Exceptions.ResourceNotFoundException;
import com.ShopIT.Models.*;
import com.ShopIT.Payloads.ApiResponse;
import com.ShopIT.Payloads.PageableDto;
import com.ShopIT.Payloads.PaymentReturnResponse;
import com.ShopIT.Repository.*;
import com.ShopIT.Security.CurrentUser;
import com.ShopIT.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.razorpay.*;
import java.security.SignatureException;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final ProductRepo productRepo;
    private final PaymentService paymentService;
    private final CartRepo cartRepo;
    private final ProfileRepo profileRepo;
    private final AddressRepo addressRepo;
    private final ProductInCartRepo productInCartRepo;
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @PostMapping("/cart/createOrder/{addressId}")
    public ResponseEntity<?> newPaymentFromCart(@CurrentUser User user, @PathVariable Long addressId) throws RazorpayException {
        Profile profile = this.profileRepo.findByUser(user);
        Address address = this.addressRepo.findById(addressId).orElseThrow(()-> new ResourceNotFoundException("Address", "addressId", addressId));
        if(!profile.getAddress().contains(address)){
            return new ResponseEntity<>(new ApiResponse("User not authorize to perform the required action", false), HttpStatus.FORBIDDEN);
        }
        if(profile.getAddress()==null || profile.getAddress().isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Please Update Your Address first", false), HttpStatus.NOT_ACCEPTABLE);
        }
        List<ProductInCart> products = this.productInCartRepo.findByUser(user);
        if(products.isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Your cart is Empty", false), HttpStatus.BAD_REQUEST);
        }
        long amt = 0L;
        for (ProductInCart product : products) {
            Product getProduct = product.getProduct();
            amt += ((long)(getProduct.getOriginalPrice()*(100-getProduct.getOfferPercentage())/100))*100*product.getNoOfProducts();
        }
        amt += 3000; // delivery
        return this.paymentService.createOrder(profile, amt, address);
    }
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @PostMapping("/createOrder/{productId}/quantity/{n}/address/{addressId}")
    public ResponseEntity<?> newPayment(@CurrentUser User user, @PathVariable("productId") Long productId, @PathVariable Long n, @PathVariable Long addressId) throws Exception {
        Profile profile = this.profileRepo.findByUser(user);
        Address address = this.addressRepo.findById(addressId).orElseThrow(()-> new ResourceNotFoundException("Address", "addressId", addressId));
        Product product = this.productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
        long amt = ((long)(product.getOriginalPrice()*(100-product.getOfferPercentage())/100))*100*n;
        amt += 3000; // delivery
        if(!profile.getAddress().contains(address)){
            return new ResponseEntity<>(new ApiResponse("User not authorize to perform the required action", false), HttpStatus.FORBIDDEN);
        }
        if(profile.getAddress()==null || profile.getAddress().isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Please Update Your Address first", false), HttpStatus.NOT_ACCEPTABLE);
        }
        return this.paymentService.createOrder(profile, amt, address);
    }
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @PostMapping("/update_order")
    public ResponseEntity<?> updateOrder(@CurrentUser User user, @RequestBody PaymentReturnResponse response) throws SignatureException {
        Profile profile = this.profileRepo.findByUser(user);
        if(profile.getAddress()==null || profile.getAddress().isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Please Update Your Address first", false), HttpStatus.NOT_ACCEPTABLE);
        }
        return this.paymentService.updateOrder(user, profile, response);
    }
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @PostMapping("/update_single_order/product/{productId}/quantity/{n}")
    public ResponseEntity<?> updateDirectOrder(@CurrentUser User user, @PathVariable Long n, @PathVariable Long productId, @RequestBody PaymentReturnResponse response) throws SignatureException {
        Profile profile = this.profileRepo.findByUser(user);
        Product product = this.productRepo.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));
        if(profile.getAddress()==null || profile.getAddress().isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Please Update Your Address first", false), HttpStatus.NOT_ACCEPTABLE);
        }
        return this.paymentService.updateDirectOrder(product, n, profile, response);
    }
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @GetMapping("/getAllOrders")
    public ResponseEntity<?> getAllMyOrders(@CurrentUser User user,
                                            @RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                            @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
                                            @RequestParam(value ="sortBy", defaultValue = "orderId", required = false) String sortBy,
                                            @RequestParam(value ="sortDir", defaultValue = "des", required = false) String sortDir
    ) {
        return new ResponseEntity<>(this.paymentService.getMyOrders(user, new PageableDto(pageNumber, pageSize, sortBy, sortDir)), OK);
    }
}
