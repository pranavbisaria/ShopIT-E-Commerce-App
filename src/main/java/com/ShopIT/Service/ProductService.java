package com.ShopIT.Service;

import com.ShopIT.Models.Images;
import com.ShopIT.Models.PageResponse;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.Categories.CategoryDTO;
import com.ShopIT.Payloads.PageableDto;
import com.ShopIT.Payloads.Products.DisplayProductDto;
import com.ShopIT.Payloads.Products.ProductDto;
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

    ResponseEntity<?> getProductById(Long productId);

    ResponseEntity<?> addProduct(User user, MultipartFile[] images, ProductDto productDto, Integer categoryId);

    ResponseEntity<?> updateProduct(User user, Long productId, ProductDto productDto);

    ResponseEntity<?> AddProductImages(User user, MultipartFile[] images, Long productId);

    ResponseEntity<?> deleteProductImages(User user, List<Images> images, Long productId);

    ResponseEntity<?> deleteProduct(User user, Long productId);

    PageResponse getAllProductByCategory(Integer categoryId, PageableDto pageable);

    ResponseEntity<?> addProductToCart(User user, Long productId);

    ResponseEntity<?> removeProductFromCart(User user, Long productId);
}
