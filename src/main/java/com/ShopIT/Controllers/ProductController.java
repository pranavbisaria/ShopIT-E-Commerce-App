package com.ShopIT.Controllers;

import com.ShopIT.Models.Images;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.PageableDto;
import com.ShopIT.Payloads.Products.ProductDto;
import com.ShopIT.Repository.ProductRepo;
import com.ShopIT.Security.CurrentUser;
import com.ShopIT.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductRepo productRepo;

//----------------------------------------------------Products-----------------------------------------------------------------------------

//Add Product
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    @PostMapping("/add/{categoryId}")
    public ResponseEntity<?> addProduct(@CurrentUser User user, @RequestPart("images") MultipartFile[] images, @RequestPart ProductDto productDto, @PathVariable("categoryId") Integer categoryId){
        return this.productService.addProduct(user, images, productDto, categoryId);
    }
    @GetMapping("/get")
    public ResponseEntity<?> getAllProducts(@RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                            @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
                                            @RequestParam(value ="sortBy", defaultValue = "productId", required = false) String sortBy,
                                            @RequestParam(value ="sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        return new ResponseEntity<>(this.productService.getAllProducts(new PageableDto(pageNumber, pageSize, sortBy, sortDir)), HttpStatus.OK);
    }
    @GetMapping("/get/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Long productId){
        return this.productService.getProductById(productId);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<?> updateProduct(@CurrentUser User user, @PathVariable("productId") Long productId, @RequestPart ProductDto productDto){
        return this.productService.updateProduct(user, productId, productDto);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    @PatchMapping("/addImages/{productId}")
    public ResponseEntity<?> addProductImages(@CurrentUser User user, @RequestParam(value = "images", required = false) MultipartFile[] images, @PathVariable("productId") Long productId){
        return this.productService.AddProductImages(user, images, productId);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    @PatchMapping("/removeImages/{productId}")
    public ResponseEntity<?> removeProductImages(@CurrentUser User user, @RequestBody List<Images> imageUrls, @PathVariable("productId") Long productId){
        return this.productService.deleteProductImages(user, imageUrls, productId);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<?> deleteMapping(@CurrentUser User user, @PathVariable("productId") Long productId){
        return this.productService.deleteProduct(user, productId);
    }
    @GetMapping("/getProductsByCategory/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable Integer categoryId,
                                                   @RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                   @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                   @RequestParam(value ="sortBy", defaultValue = "productId", required = false) String sortBy,
                                                   @RequestParam(value ="sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        return new ResponseEntity<>(this.productService.getAllProductByCategory(categoryId, new PageableDto(pageNumber, pageSize, sortBy, sortDir)), HttpStatus.OK);
    }

// ---------------------------------------------------------------CART----------------------------------------------------------------------------

    @PostMapping("/addToCart/{productId}")
    public ResponseEntity<?> addProductToCart(@CurrentUser User user, @PathVariable("productId") Long productId){
        return this.productService.addProductToCart(user, productId);
    }
    @DeleteMapping("/removeFromCart/{productId}")
    public ResponseEntity<?> removeProductFromCart(@CurrentUser User user, @PathVariable("productId") Long productId){
        return this.productService.removeProductFromCart(user, productId);
    }
//    @PutMapping()
//    public ResponseEntity<?> increaseQuantityInCart(){
//
//    }
//    @PutMapping()
//    public ResponseEntity<?> decreaseQuantityInCart(){
//
//    }
}
