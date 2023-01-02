package com.ShopIT.Service.Impl;
import com.ShopIT.Exceptions.ResourceNotFoundException;
import com.ShopIT.Models.*;
import com.ShopIT.Payloads.ApiResponse;
import com.ShopIT.Payloads.Categories.CategoryDTO;
import com.ShopIT.Payloads.PageResponse;
import com.ShopIT.Payloads.PageableDto;
import com.ShopIT.Payloads.Products.DisplayProductDto;
import com.ShopIT.Payloads.Products.ProductDto;
import com.ShopIT.Payloads.Products.ProductReturnDto;
import com.ShopIT.Repository.CartRepo;
import com.ShopIT.Repository.CategoryRepo;
import com.ShopIT.Repository.ProductRepo;
import com.ShopIT.Service.ProductService;
import com.ShopIT.Service.StorageServices;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ModelMapper modelMapper;
    private final CategoryRepo categoryRepo;
    private final StorageServices storageServices;
    private final ProductRepo productRepo;
    private final CartRepo cartRepo;
    @Override
    public ResponseEntity<?> addCategory(String CategoryName, MultipartFile photo) {
        Category category = new Category();
        category.setCategoryName(CategoryName);
        category.setImageName(this.storageServices.uploadFile(photo));
        this.categoryRepo.saveAndFlush(category);
        return new ResponseEntity<>(this.modelMapper.map(category, CategoryDTO.class), OK);
    }
    @Override
    public PageResponse getCategories(PageableDto pageable){
        Integer pN = pageable.getPageNumber(), pS = pageable.getPageSize();
        Sort sort = null;
        if(pageable.getSortDir().equalsIgnoreCase("asc")){
            sort = Sort.by(pageable.getSortBy()).ascending();
        }
        else{
            sort = Sort.by(pageable.getSortBy()).descending();
        }
        Pageable p = PageRequest.of(pN, pS, sort);
        Page<Category> pageCategory = this.categoryRepo.findAll(p);
        List<Category> allCategories = pageCategory.getContent();
        return new PageResponse(allCategories.stream().map((categories) -> this.modelMapper.map(categories, CategoryDTO.class)).collect(Collectors.toList()),pageCategory.getNumber(), pageCategory.getSize(), pageCategory.getTotalPages(), pageCategory.getTotalElements(), pageCategory.isLast());
    }
    @Override
    public ResponseEntity<?> deleteCategoryById(Integer categoryId){
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryID", categoryId));
        try {
            this.categoryRepo.delete(category);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse("This category can not be deleted", false), OK);
        }
        return new ResponseEntity<>(new ApiResponse("Category has been successfully deleted", true), OK);
    }
    @Override
    public ResponseEntity<?> changeCategoryImage(MultipartFile photo, Integer categoryId){
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryID", categoryId));
        this.storageServices.deleteFile(category.getImageName().substring(category.getImageName().lastIndexOf("/") + 1));
        category.setImageName(this.storageServices.uploadFile(photo));
        this.categoryRepo.saveAndFlush(category);
        return new ResponseEntity<>(this.modelMapper.map(category, CategoryDTO.class), OK);
    }
    @Override
    public ResponseEntity<?> updateCategory(Integer categoryId, CategoryDTO categoryDTO){
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryID", categoryId));
        category.setCategoryName(categoryDTO.getCategoryName());
        categoryRepo.saveAndFlush(category);
        return new ResponseEntity<>(this.modelMapper.map(category, CategoryDTO.class), OK);
    }
//----------------------------------------------------------------------------PRODUCTS-------------------------------------------------------------
    @Override
    public PageResponse getAllProducts(PageableDto pageable){
        Integer pN = pageable.getPageNumber(), pS = pageable.getPageSize();
        Sort sort = null;
        if(pageable.getSortDir().equalsIgnoreCase("asc")){
            sort = Sort.by(pageable.getSortBy()).ascending();
        }
        else{
            sort = Sort.by(pageable.getSortBy()).descending();
        }
        Pageable p = PageRequest.of(pN, pS, sort);
        Page<Product> pageProducts = this.productRepo.findAll(p);
        List<Product> allProducts = pageProducts.getContent();
        return new PageResponse(allProducts.stream().map((categories) -> this.modelMapper.map(categories, DisplayProductDto.class)).collect(Collectors.toList()),pageProducts.getNumber(), pageProducts.getSize(), pageProducts.getTotalPages(), pageProducts.getTotalElements(), pageProducts.isLast());
    }
    @Override
    public ResponseEntity<?> getProductById(Long productId){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        return new ResponseEntity<>(this.modelMapper.map(product, ProductReturnDto.class), OK);
    }
    @Override
    public ResponseEntity<?> addProduct(User user, MultipartFile[] images, ProductDto productDto, Integer categoryId){
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryID", categoryId));
        Product product = this.modelMapper.map(productDto, Product.class);
        if (FileValidation(images))
            return new ResponseEntity<>(new ApiResponse("File is not of image type(JPEG/ JPG or PNG)!!!", false), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        Arrays.stream(images).forEach(multipartFile -> {
            Images images1 = new Images();
            images1.setImageUrl(this.storageServices.uploadFile(multipartFile));
            product.getImageUrls().add(images1);
        });
        product.setProvider(user);
        product.getCategory().add(category);
        this.productRepo.saveAndFlush(product);
        return new ResponseEntity<>(this.modelMapper.map(product, ProductReturnDto.class), CREATED);
    }
    @Override
    public ResponseEntity<?> updateProduct(User user, Long productId, ProductDto productDto){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        if(!Objects.equals(product.getProvider().getId(), user.getId())){
            return new ResponseEntity<>(new ApiResponse("User is not authorize to perform the required action", false), HttpStatus.FORBIDDEN);
        }
        product.setProductName(productDto.getProductName());
        product.setOriginalPrice(productDto.getOriginalPrice());
        product.setOfferPercentage(productDto.getOfferPercentage());
        product.setQuantityAvailable(productDto.getQuantityAvailable());
        product.setDescription(productDto.getDescription());
        product.setWarranty(productDto.getWarranty());
        product.setHighlights(productDto.getHighlights());
        product.setServices(productDto.getServices());
        product.setSpecification(productDto.getSpecification());
        product.setQuestions(productDto.getQuestions());
        this.productRepo.saveAndFlush(product);
        return new ResponseEntity<>(this.modelMapper.map(product, ProductReturnDto.class), OK);
    }
    @Override
    public ResponseEntity<?> AddProductImages(User user, MultipartFile[] images, Long productId){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        if (FileValidation(images))
            return new ResponseEntity<>(new ApiResponse("File is not of image type(JPEG/ JPG or PNG)!!!", false), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        if(!Objects.equals(product.getProvider().getId(), user.getId())){
            return new ResponseEntity<>(new ApiResponse("User is not authorize to perform the required action", false), HttpStatus.FORBIDDEN);
        }
        Arrays.stream(images).forEach(multipartFile -> {
            product.getImageUrls().add(new Images(null, this.storageServices.uploadFile(multipartFile)));
        });
        this.productRepo.saveAndFlush(product);
        return new ResponseEntity<>(this.modelMapper.map(product, ProductReturnDto.class), OK);
    }

    private boolean FileValidation(MultipartFile[] images) {
        for (MultipartFile image : images) {
            if (!image.getContentType().equals("image/png") && !image.getContentType().equals("image/jpg") && !image.getContentType().equals("image/jpeg") && !image.getContentType().equals("image/webp")) {
                return true;
            }
        }
        return false;
    }
    @Override
    public ResponseEntity<?> deleteProductImages(User user, List<Images> images, Long productId){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        if(!Objects.equals(product.getProvider().getId(), user.getId())){
            return new ResponseEntity<>(new ApiResponse("User is not authorize to perform the required action", false), HttpStatus.FORBIDDEN);
        }
        images.forEach(multipartFile -> {
            product.getImageUrls().remove(multipartFile);
            this.storageServices.deleteFile(multipartFile.getImageUrl().substring(multipartFile.getImageUrl().lastIndexOf("/") + 1));
        });
        this.productRepo.saveAndFlush(product);
        return new ResponseEntity<>(this.modelMapper.map(product, ProductReturnDto.class), OK);
    }
    @Override
    public ResponseEntity<?> deleteProduct(User user, Long productId){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        if(!Objects.equals(product.getProvider().getId(), user.getId())) {
            return new ResponseEntity<>(new ApiResponse("User is not authorize to perform the required action", false), HttpStatus.FORBIDDEN);
        }
        this.productRepo.delete(product);
        return new ResponseEntity<>(new ApiResponse("Your Product has been successfully deleted", true), HttpStatus.OK);
    }
    @Override
    public PageResponse getAllProductByCategory(Integer categoryId, PageableDto pageable){
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Integer pN = pageable.getPageNumber(), pS = pageable.getPageSize();
        Sort sort = null;
        if(pageable.getSortDir().equalsIgnoreCase("asc")){
            sort = Sort.by(pageable.getSortBy()).ascending();
        }
        else{
            sort = Sort.by(pageable.getSortBy()).descending();
        }
        Pageable p = PageRequest.of(pN, pS, sort);
        Page<Product> pageProducts = this.productRepo.findByCategoryContaining(category, p);
        List<Product> allProducts = pageProducts.getContent();
        return new PageResponse(allProducts.stream().map((categories) -> this.modelMapper.map(categories, DisplayProductDto.class)).collect(Collectors.toList()),pageProducts.getNumber(), pageProducts.getSize(), pageProducts.getTotalPages(), pageProducts.getTotalElements(), pageProducts.isLast());

    }

//----------------------------------------------------------CART--------------------------------------------------------------------------------

    @Override
    public ResponseEntity<?> addProductToCart(User user, Long productId){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        Cart cart = user.getProfile().getCart();
        cart.getCartProducts().add(product);
        this.cartRepo.save(cart);
        return new ResponseEntity<>(new ApiResponse("Product has been successfully added to the cart", true), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> removeProductFromCart(User user, Long productId){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        Cart cart = user.getProfile().getCart();
        cart.getCartProducts().remove(product);
        this.cartRepo.save(cart);
        return new ResponseEntity<>(new ApiResponse("Product has been successfully deleted from the cart", true), HttpStatus.OK);
    }
//    @Override
//    public ResponseEntity<?> increaseProductQuantity(User user, Long productId){
//        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
//        Cart cart = user.getProfile().getCart();
//        cart.getCartProducts().add(product);
//        this.cartRepo.save(cart);
//        return new ResponseEntity<>(new ApiResponse("Product has been successfully deleted from the cart", true), HttpStatus.OK);
//    }

}
