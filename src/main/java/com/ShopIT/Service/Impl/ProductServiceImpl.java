package com.ShopIT.Service.Impl;
import com.ShopIT.Config.AppConstants;
import com.ShopIT.Exceptions.ResourceNotFoundException;
import com.ShopIT.Models.*;
import com.ShopIT.Payloads.*;
import com.ShopIT.Payloads.Categories.CategoryDTO;
import com.ShopIT.Payloads.Products.DisplayProductDto;
import com.ShopIT.Payloads.Products.ProductDto;
import com.ShopIT.Payloads.Products.ProductReturnDto;
import com.ShopIT.Repository.*;
import com.ShopIT.Service.ProductService;
import com.ShopIT.Service.StorageServices;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
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
    private final RoleRepo roleRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final WishListRepo wishListRepo;
    private final CartRepo cartRepo;
    private final ProductInCartRepo productInCartRepo;
    private final RecentProductRepo recentProductRepo;
    private final ProfileRepo profileRepo;
    private final ReviewRepo reviewRepo;
    private final QuestionsRepo questionsRepo;
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
        List<DisplayProductDto> productDTO = new ArrayList<>();
        for (Product product : allProducts) {
            DisplayProductDto productDto = this.modelMapper.map(product, DisplayProductDto.class);
            productDto.setImageUrls(product.getImageUrls().get(0));
            productDTO.add(productDto);
        }
        return new PageResponse(new ArrayList<>(productDTO),pageProducts.getNumber(), pageProducts.getSize(), pageProducts.getTotalPages(), pageProducts.getTotalElements(), pageProducts.isLast());
    }
    @Override
    public PageResponse getAllMerchantProducts(User user, PageableDto pageable){
        Integer pN = pageable.getPageNumber(), pS = pageable.getPageSize();
        Sort sort = null;
        if(pageable.getSortDir().equalsIgnoreCase("asc")){
            sort = Sort.by(pageable.getSortBy()).ascending();
        }
        else{
            sort = Sort.by(pageable.getSortBy()).descending();
        }
        Pageable p = PageRequest.of(pN, pS, sort);
        Page<Product> pageProducts = this.productRepo.findAllByProvider(p, user);
        List<Product> allProducts = pageProducts.getContent();
        List<DisplayProductDto> productDTO = new ArrayList<>();
        for (Product product : allProducts) {
            DisplayProductDto productDto = this.modelMapper.map(product, DisplayProductDto.class);
            productDto.setImageUrls(product.getImageUrls().get(0));
            productDTO.add(productDto);
        }
        return new PageResponse(new ArrayList<>(productDTO),pageProducts.getNumber(), pageProducts.getSize(), pageProducts.getTotalPages(), pageProducts.getTotalElements(), pageProducts.isLast());
    }
    @Override
    public ResponseEntity<?> getProductById(User user, Long productId) {
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        ProductReturnDto productReturnDto = null;
        productReturnDto = this.modelMapper.map(product, ProductReturnDto.class);
        if(user != null) {
            Profile profile = this.profileRepo.findByUser(user);
            RecentProduct recent = this.recentProductRepo.findByProfile(profile);
            recent.getProducts().remove(product);
            int n = recent.getProducts().size();
            if (n >= 10) {
                recent.setProducts(new ArrayList<>(recent.getProducts().subList(0, 8)));
            }
            recent.getProducts().add(0, product);
            WishList wishList = this.wishListRepo.findByProfile(profile);
            if(wishList.getProducts().contains(product)){
                productReturnDto.setAvailInWishlist(true);
            }
        }
        return new ResponseEntity<>(productReturnDto, OK);
    }
    @Override
    public ResponseEntity<?> addProduct(User user, MultipartFile[] images, ProductDto productDto, Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryID", categoryId));
        Product product = this.modelMapper.map(productDto, Product.class);
        if (FileValidation(images))
            return new ResponseEntity<>(new ApiResponse("File is not of image type(JPEG/ JPG or PNG)!!!", false), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        List<Images> productImages = new ArrayList<>(0);
        Arrays.stream(images).forEach(multipartFile -> {
            Images images1 = new Images();
            images1.setImageUrl(this.storageServices.uploadFile(multipartFile));
            productImages.add(images1);
        });
        product.setImageUrls(productImages);
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
        try {
            if (FileValidation(images))
                return new ResponseEntity<>(new ApiResponse("File is not of image type(JPEG/ JPG or PNG)!!!", false), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        catch(Exception e){
            return new ResponseEntity<>(new ApiResponse("Please select an image!!!", false), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        if (!Objects.equals(product.getProvider().getId(), user.getId())) {
            return new ResponseEntity<>(new ApiResponse("User is not authorize to perform the required action", false), HttpStatus.FORBIDDEN);
        }
        Arrays.stream(images).forEach(multipartFile -> {
            Images images1 = new Images();
            images1.setImageUrl(this.storageServices.uploadFile(multipartFile));
            product.getImageUrls().add(images1);
        });
        this.productRepo.saveAndFlush(product);
        return new ResponseEntity<>(this.modelMapper.map(product, ProductReturnDto.class), OK);
    }

    private boolean FileValidation(MultipartFile[] images) throws NullPointerException{
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
        List<DisplayProductDto> productDTO = new ArrayList<>();
        for (Product product : allProducts) {
            DisplayProductDto productDto = this.modelMapper.map(product, DisplayProductDto.class);
            productDto.setImageUrls(product.getImageUrls().get(0));
            productDTO.add(productDto);
        }
        return new PageResponse(new ArrayList<>(productDTO),pageProducts.getNumber(), pageProducts.getSize(), pageProducts.getTotalPages(), pageProducts.getTotalElements(), pageProducts.isLast());
    }
    @Override
    public PageResponse getAllProductByCategoryMerchant(User user, Integer categoryId, PageableDto pageable){
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
        Page<Product> pageProducts = this.productRepo.findByCategoryContainingAndProvider(category, user, p);
        List<Product> allProducts = pageProducts.getContent();
        List<DisplayProductDto> productDTO = new ArrayList<>();
        for (Product product : allProducts) {
            DisplayProductDto productDto = this.modelMapper.map(product, DisplayProductDto.class);
            productDto.setImageUrls(product.getImageUrls().get(0));
            productDTO.add(productDto);
        }
        return new PageResponse(new ArrayList<>(productDTO),pageProducts.getNumber(), pageProducts.getSize(), pageProducts.getTotalPages(), pageProducts.getTotalElements(), pageProducts.isLast());
    }

//----------------------------------------------------------CART--------------------------------------------------------------------------------

    @Override
    public ResponseEntity<?> addProductToCart(User user, Long productId, Long n){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: " + productId, 0));
        if(this.productInCartRepo.existsByProductAndUser(product, user)){
            return new ResponseEntity<>(new ApiResponse("Product already exist in the cart", true), HttpStatus.CONFLICT);
        }
        if(n >= product.getQuantityAllowedPerUser()){
            return  new ResponseEntity<>(new ApiResponse("Product has reached its maximum quantity", false), HttpStatus.METHOD_NOT_ALLOWED);
        }
        Profile profile = this.profileRepo.findByUser(user);
        Cart cart = this.cartRepo.findByProfile(profile);
        ProductInCart productInCart = new ProductInCart();
        productInCart.setProduct(product);
        productInCart.setNoOfProducts(n);
        productInCart.setDateOfOrder(new Date(System.currentTimeMillis()));
        productInCart.setUser(user);
        cart.getCartProducts().add(productInCart);
        this.cartRepo.save(cart);
        return new ResponseEntity<>(new ApiResponse("Product has been successfully added to the cart", true), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> removeProductFromCart(User user, Long productId){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        Profile profile = this.profileRepo.findByUser(user);
        Cart cart = this.cartRepo.findByProfile(profile);
        ProductInCart productInCart = this.productInCartRepo.findByProductAndUser(product, user).orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
        cart.getCartProducts().remove(productInCart);
        this.productInCartRepo.delete(productInCart);
        this.cartRepo.save(cart);
        return new ResponseEntity<>(new ApiResponse("Product has been successfully deleted from the cart", true), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> deleteByProductInCart(User user, Long Id){
        this.productInCartRepo.deleteByIdAndUser(Id, user);
        return new ResponseEntity<>(new ApiResponse("Product has been successfully deleted from the cart", true), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> emptyMyCart(User user){
        List<ProductInCart> product = this.productInCartRepo.findByUser(user);
        product.forEach((productInCart -> {
            this.productInCartRepo.deleteById(productInCart.getId());
        }));
        return new ResponseEntity<>(new ApiResponse("All products have been successfully deleted from the cart", true), HttpStatus.OK);
    }
    @Override
    public PageResponse getAllProductsInCart(User user, PageableDto pageable){
        Integer pN = pageable.getPageNumber(), pS = pageable.getPageSize();
        Sort sort = null;
        if(pageable.getSortDir().equalsIgnoreCase("asc")){
            sort = Sort.by(pageable.getSortBy()).ascending();
        }
        else{
            sort = Sort.by(pageable.getSortBy()).descending();
        }
        Pageable p = PageRequest.of(pN, pS, sort);
        Page<ProductInCart> pageProducts = this.productInCartRepo.findByUser(user, p);
        List<ProductInCart> allProducts = pageProducts.getContent();
        List<ProductInCartDto> productDTO = new ArrayList<>();
        for (ProductInCart product : allProducts) {
            ProductInCartDto productDto = this.modelMapper.map(product, ProductInCartDto.class);
            productDto.getProduct().setImageUrls(product.getProduct().getImageUrls().get(0));
            productDto.setAvailable(product.getProduct().getQuantityAvailable() >= product.getNoOfProducts());
            productDTO.add(productDto);
        }
        return new PageResponse(new ArrayList<>(productDTO), pageProducts.getNumber(), pageProducts.getSize(), pageProducts.getTotalPages(), pageProducts.getTotalElements(), pageProducts.isLast());
    }
    @Override
    public ResponseEntity<?> increaseProductQuantity(User user, Long productId){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        ProductInCart productInCart = this.productInCartRepo.findByProductAndUser(product, user).orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
        if(productInCart.getNoOfProducts() >= product.getQuantityAllowedPerUser()){
            return  new ResponseEntity<>(new ApiResponse("Product has reached its maximum quantity", false), HttpStatus.METHOD_NOT_ALLOWED);
        }
        productInCart.setNoOfProducts(productInCart.getNoOfProducts() +1L);
        return new ResponseEntity<>(new ApiResponse("Product quantity has been successfully increased", true), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> decreaseProductQuantity(User user, Long productId){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        ProductInCart productInCart = this.productInCartRepo.findByProductAndUser(product, user).orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
        if((productInCart.getNoOfProducts()-1L) <= 0L){
           return deleteByProductInCart(user, productInCart.getId());
        }
        productInCart.setNoOfProducts(productInCart.getNoOfProducts() - 1L);
        return new ResponseEntity<>(new ApiResponse("Product quantity has been successfully decreased", true), HttpStatus.OK);
    }

//------------------------------------------------------------------WishList-------------------------------------------------------

    @Override
    public ResponseEntity<?> addToWishlist(User user, Long productId){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        Profile profile = this.profileRepo.findByUser(user);
        WishList wishList = this.wishListRepo.findByProfile(profile);
        if(wishList.getProducts().contains(product)){
            wishList.getProducts().remove(product);
            return new ResponseEntity<>(new ApiResponse("Product removed from WishList", true), HttpStatus.OK);
        }
        wishList.getProducts().add(product);
        return new ResponseEntity<>(new ApiResponse("Product Added To WishList", true), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> removeFromWishlist(User user, Long productId){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        Profile profile = this.profileRepo.findByUser(user);
        WishList wishList = this.wishListRepo.findByProfile(profile);
        if(!wishList.getProducts().contains(product)){
            return new ResponseEntity<>(new ApiResponse("No product found in your Wishlist!!", false), HttpStatus.NOT_FOUND);
        }
        wishList.getProducts().remove(product);
        return new ResponseEntity<>(new ApiResponse("Product removed from WishList", true), HttpStatus.OK);
    }
    @Override
    public PageResponse getWishList(User user, PageableDto pageable){
        Integer pN = pageable.getPageNumber(), pS = pageable.getPageSize();
        Sort sort = null;
        if(pageable.getSortDir().equalsIgnoreCase("asc")){
            sort = Sort.by(pageable.getSortBy()).ascending();
        }
        else{
            sort = Sort.by(pageable.getSortBy()).descending();
        }
        Pageable p = PageRequest.of(pN, pS, sort);
        Profile profile = this.profileRepo.findByUser(user);
        WishList wishList = this.wishListRepo.findByProfile(profile);
        Page<Product> pageProducts = new PageImpl<>(wishList.getProducts().stream().toList(), p, pageable.getPageSize());
        List<Product> allProducts = pageProducts.getContent();
        List<DisplayProductDto> productDTO = new ArrayList<>();
        for (Product product : allProducts) {
            DisplayProductDto productDto = this.modelMapper.map(product, DisplayProductDto.class);
            productDto.setImageUrls(product.getImageUrls().get(0));
            productDTO.add(productDto);
        }
        return new PageResponse(new ArrayList<>(productDTO),pageProducts.getNumber(), pageProducts.getSize(), pageProducts.getTotalPages(), pageProducts.getTotalElements(), pageProducts.isLast());
    }

//---------------------------------------------------------Recent Products-------------------------------------------------------------------------
    @Transactional
    @Override
    public ResponseEntity<?> getRecentProducts(User user){
        Profile profile = this.profileRepo.findByUser(user);
        RecentProduct recentProduct = this.recentProductRepo.findByProfile(profile);
//        Set<Product> set = new LinkedHashSet<>(recentProduct.getProducts());
//        recentProduct.getProducts().clear();
//        recentProduct.getProducts().addAll(set);
        RecentProductDto recent = new RecentProductDto(recentProduct.getId(), new ArrayList<>());
        for(Product product : recentProduct.getProducts()) {
            DisplayProductDto productDto = this.modelMapper.map(product, DisplayProductDto.class);
            productDto.setImageUrls(product.getImageUrls().get(0));
            recent.getProducts().add(productDto);
        }
        return new ResponseEntity<>(recent, OK);
    }
// --------------------------------------------------Rating&Review--------------------------------------------------
    @Override
    public ResponseEntity<?> addReview(User user, Long productId, ReviewDto reviewDto, MultipartFile[] images){
        if (FileValidation(images))
            return new ResponseEntity<>(new ApiResponse("File is not of image type(JPEG/ JPG or PNG)!!!", false), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        Profile profile = this.profileRepo.findByUser(user);
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID: "+productId, 0));
        if(this.reviewRepo.existsByProfilesAndProduct(profile, product)){
            return new ResponseEntity<>(new ApiResponse("User review already exist!!", false), HttpStatus.CONFLICT);
        }
        long noOfRating = this.reviewRepo.countByProduct(product);
        //formula used ==>        ((Overall Rating * Total Rating) + new Rating) / (Total Rating + 1)
        float newRating = (((product.getRating() * noOfRating) + Integer.parseInt(reviewDto.getRating()))/ (noOfRating + 1f));
        product.setRating(newRating);
        this.productRepo.save(product);
        Review review = new Review();
        review.setRating(Integer.parseInt(reviewDto.getRating()));
        review.setDescription(reviewDto.getDescription());
        if(!Objects.isNull(images)){
            Arrays.stream(images).forEach(multipartFile -> {
                Images rImages = new Images();
                rImages.setImageUrl(this.storageServices.uploadFile(multipartFile));
                review.getImages().add(rImages);
            });
        }
        review.setProfiles(profile);
        review.setProduct(product);
        review.setIssueTime(new Date(System.currentTimeMillis()));
        this.reviewRepo.save(review);
        reviewDto.setId(review.getId());
        reviewDto.setImages(review.getImages());
        reviewDto.setUser(this.modelMapper.map(user, UserShow.class));
        reviewDto.setIssueTime(review.getIssueTime());
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }
    @Override
    public PageResponse getReviews(Long productId, PageableDto pageable){
        Integer pN = pageable.getPageNumber(), pS = pageable.getPageSize();
        Sort sort = null;
        if(pageable.getSortDir().equalsIgnoreCase("asc")){
            sort = Sort.by(pageable.getSortBy()).ascending();
        }
        else{
            sort = Sort.by(pageable.getSortBy()).descending();
        }
        Pageable p = PageRequest.of(pN, pS, sort);
        Product product = this.productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));
        Page<Review> pageReview = this.reviewRepo.findAllByProduct(product, p);
        List<Review> productReview = pageReview.getContent();
        List<ReviewDto> reviewDTOs = new ArrayList<>();
        productReview.forEach((ReviewProd)-> {
            ReviewDto reviewDto = this.modelMapper.map(ReviewProd, ReviewDto.class);
            reviewDto.setUser(this.modelMapper.map(this.userRepo.findByProfile(ReviewProd.getProfiles()), UserShow.class));
            reviewDTOs.add(reviewDto);
        });
        return new PageResponse(new ArrayList<>(reviewDTOs), pageReview.getNumber(), pageReview.getSize(), pageReview.getTotalPages(), pageReview.getTotalElements(), pageReview.isLast());
    }
    @Override
    public ResponseEntity<?> getMyProductReview(User user, Long productId){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        Profile profile = this.profileRepo.findByUser(user);
        ReviewDto reviewDto = this.modelMapper.map(this.reviewRepo.findByProductAndProfiles(product, profile), ReviewDto.class);
        reviewDto.setUser(this.modelMapper.map(user, UserShow.class));
        return new ResponseEntity<>(reviewDto, OK);
    }
    @Override
    public PageResponse getMyReviews(User user, PageableDto pageable){
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
        Page<Review> pageReview = this.reviewRepo.findAllByProfiles(profile, p);
        List<Review> productReview = pageReview.getContent();
        List<ReviewDto> reviewDTOs = new ArrayList<>();
        productReview.forEach((ReviewProd)-> {
            ReviewDto reviewDto = this.modelMapper.map(ReviewProd, ReviewDto.class);
            reviewDto.setUser(this.modelMapper.map(this.userRepo.findByProfile(ReviewProd.getProfiles()), UserShow.class));
            reviewDTOs.add(reviewDto);
        });
        return new PageResponse(new ArrayList<>(reviewDTOs), pageReview.getNumber(), pageReview.getSize(), pageReview.getTotalPages(), pageReview.getTotalElements(), pageReview.isLast());
    }

//-----------------------------------------------------------------------Search ----------------------------------------------------------------------
//merchant side
    @Override
    public PageResponse searchAll(String keyword, PageableDto pageable, double minRating, double maxRating, double minPrice, double maxPrice){
        Integer pN = pageable.getPageNumber(), pS = pageable.getPageSize();
        Page<Product> pageProducts = null;
        if(pageable.getSortBy().equals("productId") || pageable.getSortBy().equals("originalPrice") || pageable.getSortBy().equals("offerPercentage") || pageable.getSortBy().equals("rating")){
            Sort sort = Sort.by(pageable.getSortBy()).ascending();
            if(pageable.getSortDir().equals("dsc") || pageable.getSortDir().equals("DSC")) {
                sort = Sort.by(pageable.getSortBy()).descending();
            }
            Pageable p = PageRequest.of(pN, pS, sort);
            pageProducts = this.productRepo.findByProductNameContainingIgnoreCase(keyword, p, minPrice, maxPrice, minRating, maxRating);
        }
        else{
            Pageable p = PageRequest.of(pN, pS);
            pageProducts = this.productRepo.findByProductNameIgnoreCase(keyword, p, minPrice, maxPrice, minRating,  maxRating);
        }
        List<Product> allProducts = pageProducts.getContent();
        List<DisplayProductDto> productDTO = new ArrayList<>();
        for (Product product : allProducts) {
            DisplayProductDto productDto = this.modelMapper.map(product, DisplayProductDto.class);
            productDto.setImageUrls(product.getImageUrls().get(0));
            productDTO.add(productDto);
        }
        return new PageResponse(productDTO.stream().sequential().collect(Collectors.toList()), pageProducts.getNumber(), pageProducts.getSize(), pageProducts.getTotalPages(), pageProducts.getTotalElements(), pageProducts.isLast());
    }
//merchant side
    @Override
    public PageResponse searchAllMerchant(User user, String keyword, PageableDto pageable, double minRating, double maxRating, double minPrice, double maxPrice){
        Integer pN = pageable.getPageNumber(), pS = pageable.getPageSize();
        Page<Product> pageProducts = null;
        if(pageable.getSortBy().equals("productId") || pageable.getSortBy().equals("originalPrice") || pageable.getSortBy().equals("offerPercentage") || pageable.getSortBy().equals("rating")){
            Sort sort = Sort.by(pageable.getSortBy()).ascending();
            if(pageable.getSortDir().equals("dsc") || pageable.getSortDir().equals("DSC")) {
                sort = Sort.by(pageable.getSortBy()).descending();
            }
            Pageable p = PageRequest.of(pN, pS, sort);
            pageProducts = this.productRepo.findByProductNameContainingIgnoreCaseAndProvider(keyword, p, minPrice, maxPrice, minRating, maxRating, user);
        }
        else{
            Pageable p = PageRequest.of(pN, pS);
            pageProducts = this.productRepo.findByProductNameIgnoreCaseAndProvider(keyword, p, minPrice, maxPrice, minRating,  maxRating, user.getId());
        }
        List<Product> allProducts = pageProducts.getContent();
        List<DisplayProductDto> productDTO = new ArrayList<>();
        for (Product product : allProducts) {
            DisplayProductDto productDto = this.modelMapper.map(product, DisplayProductDto.class);
            productDto.setImageUrls(product.getImageUrls().get(0));
            productDTO.add(productDto);
        }
        return new PageResponse(productDTO.stream().sequential().collect(Collectors.toList()), pageProducts.getNumber(), pageProducts.getSize(), pageProducts.getTotalPages(), pageProducts.getTotalElements(), pageProducts.isLast());
    }
//---------------------------------------------------FAQs------------------------------------------------------------------------------------
    @Override
    public PageResponse getAllFAQ(Long productId, PageableDto pageable){
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        Integer pN = pageable.getPageNumber(), pS = pageable.getPageSize();
        Pageable p = PageRequest.of(pN, pS);
        Set<QuestionModel> questions = product.getQuestions();
        Page<QuestionModel> pageFAQ = new PageImpl<>(questions.stream().toList(), p, pageable.getPageSize());
        List<QuestionModel> allFAQs = pageFAQ.getContent();
        return new PageResponse(new ArrayList<>(allFAQs),pageFAQ.getNumber(), pageFAQ.getSize(), pageFAQ.getTotalPages(), pageFAQ.getTotalElements(), pageFAQ.isLast());
    }
    @Override
    public ResponseEntity<?> addFAQ(User user, Long ProductId, QuestionModel questionModel){
        Product product = this.productRepo.findById(ProductId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID", ProductId));
        if(!user.getRoles().contains(this.roleRepo.findById(AppConstants.ROLE_ADMIN).orElse(null)) && !user.getRoles().contains(this.roleRepo.findById(AppConstants.ROLE_MERCHANT).orElse(null))) questionModel.setAnswer(null);
        product.getQuestions().add(questionModel);
        this.productRepo.save(product);
        return new ResponseEntity<>(new ApiResponse("Question has been successfully added", true), OK);
    }
    @Override
    public ResponseEntity<?> answerAFAQ(User user, Long ProductId, QuestionModel questionModel, Integer QuestionID){
        QuestionModel questionModel1 = this.questionsRepo.findById(QuestionID).orElseThrow(()-> new ResourceNotFoundException("Question", "QuestionID", QuestionID));
        Product product = this.productRepo.findById(ProductId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductID", ProductId));
        if(!Objects.equals(product.getProvider().getId(), user.getId())){
            return new ResponseEntity<>(new ApiResponse("User is not authorize to perform the required action", false), HttpStatus.FORBIDDEN);
        }
        questionModel1.setAnswer(questionModel1.getAnswer());
        return new ResponseEntity<>(questionModel1, OK);
    }
}
