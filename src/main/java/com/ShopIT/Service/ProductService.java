package com.ShopIT.Service;

import com.ShopIT.Models.Images;
import com.ShopIT.Models.QuestionModel;
import com.ShopIT.Payloads.PageResponse;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.Categories.CategoryDTO;
import com.ShopIT.Payloads.PageableDto;
import com.ShopIT.Payloads.Products.ProductDto;
import com.ShopIT.Payloads.ReviewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ResponseEntity<?> addCategory(String CategoryName, MultipartFile photo);

    PageResponse getCategories(PageableDto pageable);

    ResponseEntity<?> deleteCategoryById(Integer categoryId);

    ResponseEntity<?> changeCategoryImage(MultipartFile photo, Integer categoryId);

    ResponseEntity<?> updateCategory(Integer categoryId, CategoryDTO categoryDTO);

    //----------------------------------------------------------------------------PRODUCTS-------------------------------------------------------------
    PageResponse getAllProducts(PageableDto pageable);

    PageResponse getAllMerchantProducts(User user, PageableDto pageable);

    ResponseEntity<?> getProductById(User user, Long productId);

    ResponseEntity<?> addProduct(User user, MultipartFile[] images, ProductDto productDto, Integer categoryId);

    ResponseEntity<?> updateProduct(User user, Long productId, ProductDto productDto);

    ResponseEntity<?> AddProductImages(User user, MultipartFile[] images, Long productId);

    ResponseEntity<?> deleteProductImages(User user, List<Images> images, Long productId);

    ResponseEntity<?> deleteProduct(User user, Long productId);

    PageResponse getAllProductByCategory(Integer categoryId, PageableDto pageable);

    PageResponse getAllProductByCategoryMerchant(User user, Integer categoryId, PageableDto pageable);

    ResponseEntity<?> addProductToCart(User user, Long productId, Long n);

    ResponseEntity<?> removeProductFromCart(User user, Long productId);

    ResponseEntity<?> deleteByProductInCart(User user, Long Id);

    ResponseEntity<?> emptyMyCart(User user);

    PageResponse getAllProductsInCart(User user, PageableDto pageable);

    ResponseEntity<?> increaseProductQuantity(User user, Long productId);

    ResponseEntity<?> decreaseProductQuantity(User user, Long productId);

    ResponseEntity<?> addToWishlist(User user, Long productId);

    ResponseEntity<?> removeFromWishlist(User user, Long productId);

    PageResponse getWishList(User user, PageableDto pageable);

    //---------------------------------------------------------Recent Products-------------------------------------------------------------------------
    ResponseEntity<?> getRecentProducts(User user);

    // --------------------------------------------------Rating&Review--------------------------------------------------
    ResponseEntity<?> addReview(User user, Long productId, ReviewDto reviewDto, MultipartFile[] images);

    PageResponse getReviews(Long productId, PageableDto pageable);

    ResponseEntity<?> getMyProductReview(User user, Long productId);

    PageResponse getMyReviews(User user, PageableDto pageable);

    PageResponse searchAll(String keyword, PageableDto pageable, double minRating, double maxRating, double minPrice, double maxPrice);

    //merchant side
    PageResponse searchAllMerchant(User user, String keyword, PageableDto pageable, double minRating, double maxRating, double minPrice, double maxPrice);

    //---------------------------------------------------FAQs------------------------------------------------------------------------------------
    PageResponse getAllFAQ(Long productId, PageableDto pageable);

    ResponseEntity<?> addFAQ(User user, Long ProductId, QuestionModel questionModel);

    ResponseEntity<?> answerAFAQ(User user, Long ProductId, QuestionModel questionModel, Integer QuestionID);
}
