package com.ShopIT.Controllers;
import com.ShopIT.Exceptions.ResourceNotFoundException;
import com.ShopIT.Models.*;
import com.ShopIT.Payloads.ApiResponse;
import com.ShopIT.Payloads.PaymentReturnResponse;
import com.ShopIT.Repository.ProductRepo;
import com.ShopIT.Security.CurrentUser;
import com.ShopIT.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.razorpay.*;
import java.security.SignatureException;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final ProductRepo productRepo;
    private final PaymentService paymentService;
    @PostMapping("/cart/createOrder")
    public ResponseEntity<?> newPaymentFromCart(@CurrentUser User user) throws RazorpayException {
        if(user.getProfile().getCart().getCartProducts() == null){
            return new ResponseEntity<>(new ApiResponse("Invalid Action!!!", false), HttpStatus.BAD_REQUEST);
        }
        if(user.getProfile().getAddress()==null || user.getProfile().getAddress().isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Please Update Your Address first", false), HttpStatus.NOT_ACCEPTABLE);
        }
        List<ProductInCart> products = user.getProfile().getCart().getCartProducts();
        long amt = 0L;
        for (ProductInCart product : products) {
            Product getProduct = product.getProduct();
            amt += (long)(getProduct.getOriginalPrice()*(100-getProduct.getOfferPercentage())*product.getNoOfProducts());
        }
        if(amt == 0L){
            return new ResponseEntity<>(new ApiResponse("Invalid Action!!!", false), HttpStatus.BAD_REQUEST);
        }
        return this.paymentService.createOrder(user, amt);
    }
    @PostMapping("/createOrder/{productId}")
    public ResponseEntity<?> newPayment(@CurrentUser User user, @PathVariable("productId") Long productId) throws Exception {
        Product product = this.productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
        long amt = (long)(product.getOriginalPrice()*(100-product.getOfferPercentage()));
        if(user.getProfile().getAddress()==null || user.getProfile().getAddress().isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Please Update Your Address first", false), HttpStatus.NOT_ACCEPTABLE);
        }
        return this.paymentService.createOrder(user, amt);
    }
    @PostMapping("/update_order")
    public ResponseEntity<?> updateOrder(@CurrentUser User user, @RequestBody PaymentReturnResponse response) throws SignatureException {
        if(user.getProfile().getAddress()==null || user.getProfile().getAddress().isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Please Update Your Address first", false), HttpStatus.NOT_ACCEPTABLE);
        }
        return this.paymentService.updateOrder(user, response);
    }
}
