package com.ShopIT.Controllers;
import com.ShopIT.Models.*;
import com.ShopIT.Payloads.ApiResponse;
import com.ShopIT.Payloads.PageableDto;
import com.ShopIT.Payloads.Products.ProductDto;
import com.ShopIT.Payloads.ReviewDto;
import com.ShopIT.Security.CurrentUser;
import com.ShopIT.Service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

//-------------------------------------------------------------Products-------------------------------------------------------------------------------
//Add Product
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    @PostMapping("/add/{categoryId}")
    public ResponseEntity<?> addProduct(@CurrentUser User user, @RequestPart("images") MultipartFile[] images,@Valid @RequestPart ProductDto productDto, @PathVariable("categoryId") Integer categoryId) {
        return this.productService.addProduct(user, images, productDto, categoryId);
    }
    //Get all product User Side
    @GetMapping("/get")
    public ResponseEntity<?> getAllProducts(@RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                            @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
                                            @RequestParam(value ="sortBy", defaultValue = "productId", required = false) String sortBy,
                                            @RequestParam(value ="sortDir", defaultValue = "des", required = false) String sortDir
    ){
        return new ResponseEntity<>(this.productService.getAllProducts(new PageableDto(pageNumber, pageSize, sortBy, sortDir)), OK);
    }
    //Get all product Merchant Side
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    @GetMapping("/getAllMerchantProducts")
    public ResponseEntity<?> getAllMerchantProducts(@CurrentUser User user,
                                            @RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                            @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
                                            @RequestParam(value ="sortBy", defaultValue = "productId", required = false) String sortBy,
                                            @RequestParam(value ="sortDir", defaultValue = "des", required = false) String sortDir
    ){
        return new ResponseEntity<>(this.productService.getAllMerchantProducts(user, new PageableDto(pageNumber, pageSize, sortBy, sortDir)), OK);
    }
    @GetMapping("/get/{productId}")
    public ResponseEntity<?> getProductById(@CurrentUser User user, @PathVariable("productId") Long productId) {
        return this.productService.getProductById(user, productId);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<?> updateProduct(@CurrentUser User user, @PathVariable("productId") Long productId,@Valid @RequestPart ProductDto productDto){
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
    //Category Product In User Side
    @GetMapping("/getProductsByCategory/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable("categoryId") Integer categoryId,
                                                   @RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                   @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                   @RequestParam(value ="sortBy", defaultValue = "productId", required = false) String sortBy,
                                                   @RequestParam(value ="sortDir", defaultValue = "des", required = false) String sortDir
    ){
        return new ResponseEntity<>(this.productService.getAllProductByCategory(categoryId, new PageableDto(pageNumber, pageSize, sortBy, sortDir)), OK);
    }
    //Category Product In Merchant Side
    @GetMapping("/getProductsByCategoryMerchant/{categoryId}")
    public ResponseEntity<?> getProductsByCategoryInMerchantSide(@CurrentUser User user,
                                                   @PathVariable("categoryId") Integer categoryId,
                                                   @RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                   @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                   @RequestParam(value ="sortBy", defaultValue = "productId", required = false) String sortBy,
                                                   @RequestParam(value ="sortDir", defaultValue = "des", required = false) String sortDir
    ){
        return new ResponseEntity<>(this.productService.getAllProductByCategoryMerchant(user, categoryId, new PageableDto(pageNumber, pageSize, sortBy, sortDir)), OK);
    }

// ---------------------------------------------------------------CART----------------------------------------------------------------------------

    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL')")
    @PostMapping("/addToCart/{productId}/quantity/{n}")
    public ResponseEntity<?> addProductToCart(@CurrentUser User user, @PathVariable("productId") Long productId, @PathVariable("n") Long n) throws Exception{
        if(user == null){
            return new ResponseEntity<>(new ApiResponse("Invalid Action!!", false), HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            return this.productService.addProductToCart(user, productId, n);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/remove/productInCart/{id}")
    public ResponseEntity<?> deleteTheProductInCart(@CurrentUser User user, @PathVariable Long id){
        return this.productService.deleteByProductInCart(user, id);
    }
    @DeleteMapping("/remove/emptyMyCart")
    public ResponseEntity<?> deleteTheProductInCart(@CurrentUser User user){
        return this.productService.emptyMyCart(user);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL')")
    @GetMapping("/cart/get")
    public ResponseEntity<?> getAllCart(@CurrentUser User user,
                                        @RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                        @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
                                        @RequestParam(value ="sortBy", defaultValue = "productId", required = false) String sortBy,
                                        @RequestParam(value ="sortDir", defaultValue = "des", required = false) String sortDir
    ) throws Exception{
        if(user ==null){
            return new ResponseEntity<>(new ApiResponse("Invalid Action!!", false), HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            return new ResponseEntity<>(this.productService.getAllProductsInCart(user, new PageableDto(pageNumber, pageSize, sortBy, sortDir)), OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL')")
    @DeleteMapping("/removeFromCart/{productId}")
    public ResponseEntity<?> removeProductFromCart(@CurrentUser User user, @PathVariable("productId") Long productId) {
        if(user ==null){
            return new ResponseEntity<>(new ApiResponse("Invalid Action!!", false), HttpStatus.NOT_ACCEPTABLE);
        }
        return this.productService.removeProductFromCart(user, productId);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL')")
    @PutMapping("/cart/increase/{productId}")
    public ResponseEntity<?> increaseQuantityInCart(@CurrentUser User user, @PathVariable("productId") Long productId){
        if(user ==null){
            return new ResponseEntity<>(new ApiResponse("Invalid Action!!", false), HttpStatus.NOT_ACCEPTABLE);
        }
        return this.productService.increaseProductQuantity(user, productId);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL')")
    @PutMapping("/cart/decrease/{productId}")
    public ResponseEntity<?> decreaseQuantityInCart(@CurrentUser User user, @PathVariable("productId") Long productId){
        if(user ==null){
            return new ResponseEntity<>(new ApiResponse("Invalid Action!!", false), HttpStatus.NOT_ACCEPTABLE);
        }
        return this.productService.decreaseProductQuantity(user, productId);
    }

//-------------------------------------------------------------------------WishList--------------------------------------------------------------
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL')")
    @PatchMapping("/wishlist/add/{productId}")
    public ResponseEntity<?> addToWishList(@CurrentUser User user, @PathVariable("productId") Long productId){
        return this.productService.addToWishlist(user, productId);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL')")
    @PatchMapping("/wishlist/remove/{productId}")
    public ResponseEntity<?> removeFromWishList(@CurrentUser User user, @PathVariable("productId") Long productId){
        return this.productService.removeFromWishlist(user, productId);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL')")
    @GetMapping("/wishlist/get")
    public ResponseEntity<?> getAllProductInAWishlist(@CurrentUser User user,
                                                      @RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                      @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                      @RequestParam(value ="sortBy", defaultValue = "productId", required = false) String sortBy,
                                                      @RequestParam(value ="sortDir", defaultValue = "des", required = false) String sortDir
    ) throws Exception{
        if(user==null){
            return new ResponseEntity<>(new ApiResponse("Invalid Action!!", false), HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            return new ResponseEntity<>(this.productService.getWishList(user, new PageableDto(pageNumber, pageSize, sortBy, sortDir)), OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//---------------------------------------------Search & Filter-----------------------------------------------------------------------
    //user side
    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> searchProduct(@PathVariable("keyword") String keyword,
                                           @RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                           @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
                                           @RequestParam(value ="sortBy", defaultValue = "productName", required = false) String sortBy,
                                           @RequestParam(value ="sortDir", defaultValue = "des", required = false) String sortDir,
                                           @RequestParam(value ="minRating", defaultValue = "0", required = false) double minRating,
                                           @RequestParam(value ="maxRating", defaultValue = "5", required = false) double maxRating,
                                           @RequestParam(value ="minPrice", defaultValue = "0", required = false) double minPrice,
                                           @RequestParam(value ="maxPrice", defaultValue = "9999999", required = false) double maxPrice
    ) throws Exception{
        return new ResponseEntity<>(this.productService.searchAll(keyword, new PageableDto(pageNumber, pageSize, sortBy, sortDir), minRating, maxRating, minPrice, maxPrice), OK);
    }
    //Search In Merchant Profile
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    @GetMapping("/searchMerchant/{keyword}")
    public ResponseEntity<?> searchProductMerchant(@CurrentUser User user,
                                           @PathVariable("keyword") String keyword,
                                           @RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                           @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
                                           @RequestParam(value ="sortBy", defaultValue = "productName", required = false) String sortBy,
                                           @RequestParam(value ="sortDir", defaultValue = "des", required = false) String sortDir,
                                           @RequestParam(value ="minRating", defaultValue = "0", required = false) double minRating,
                                           @RequestParam(value ="maxRating", defaultValue = "5", required = false) double maxRating,
                                           @RequestParam(value ="minPrice", defaultValue = "0", required = false) double minPrice,
                                           @RequestParam(value ="maxPrice", defaultValue = "9999999", required = false) double maxPrice
    ) throws Exception{
        return new ResponseEntity<>(this.productService.searchAllMerchant(user, keyword, new PageableDto(pageNumber, pageSize, sortBy, sortDir), minRating, maxRating, minPrice, maxPrice), OK);
    }

//----------------------------------------------Rating and reviews -------------------------------------------------------------------
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @PostMapping("/review/add/{productId}")
        public ResponseEntity<?> addAReview(@CurrentUser User user, @RequestParam("images") MultipartFile[] images, @PathVariable(value = "productId", required = false) Long productId, @Valid @NotNull @RequestParam("rating") String rating, @Valid @RequestParam(required = false) String description) {
        return this.productService.addReview(user, productId, new ReviewDto(null, null, rating, description, null, null), images);
    }
    @GetMapping("/getReview/{productId}")
    public ResponseEntity<?> getAllReviews(@PathVariable(value = "productId", required = false) Long productId,
                                           @RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                           @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
                                           @RequestParam(value ="sortBy", defaultValue = "id", required = false) String sortBy,
                                           @RequestParam(value ="sortDir", defaultValue = "des", required = false) String sortDir
    ) {
        return new ResponseEntity<>(this.productService.getReviews(productId, new PageableDto(pageNumber, pageSize, sortBy, sortDir)), OK);
    }
    @GetMapping("/getMyReviews/{productId}")
    public ResponseEntity<?> getMyReviewForProduct(@CurrentUser User user, @PathVariable("productId") Long productId){
        return this.productService.getMyProductReview(user, productId);
    }
    @GetMapping("/getALLMyReviews")
    public ResponseEntity<?> getAllReviews(@CurrentUser User user,
                                           @RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                           @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
                                           @RequestParam(value ="sortBy", defaultValue = "id", required = false) String sortBy,
                                           @RequestParam(value ="sortDir", defaultValue = "des", required = false) String sortDir
    ) {
        return new ResponseEntity<>(this.productService.getMyReviews(user, new PageableDto(pageNumber, pageSize, sortBy, sortDir)), OK);
    }

//------------------------------------------FAQs---------------------------------------------------------------------------------------
    @GetMapping("/getFAQs/{ProductId}")
    public ResponseEntity<?> getAllFAQa(@PathVariable Long ProductId,
                                        @RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                        @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize
    ) {
        return new ResponseEntity<>(this.productService.getAllFAQ(ProductId, new PageableDto(pageNumber, pageSize, null, null)), OK);
    }
    @PostMapping("/addFAQ/{ProductId}")
    public ResponseEntity<?> addMyQuestion(@CurrentUser User user, @PathVariable Long ProductId, @RequestBody QuestionModel questionModel){
        return this.productService.addFAQ(user, ProductId, questionModel);
    }
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    @PatchMapping("/answerFAQ/product/{ProductId}/question/{QuestionID}")
    public ResponseEntity<?> answerAFAQ(@CurrentUser User user, @PathVariable Long ProductId, @PathVariable Integer QuestionID, @RequestBody QuestionModel questionModel){
        return this.productService.answerAFAQ(user, ProductId, questionModel, QuestionID);
    }
}
