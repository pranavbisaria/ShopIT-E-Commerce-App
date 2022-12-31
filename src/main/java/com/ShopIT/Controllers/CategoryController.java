package com.ShopIT.Controllers;

import com.ShopIT.Payloads.Categories.CategoryDTO;
import com.ShopIT.Payloads.PageableDto;
import com.ShopIT.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ProductService productService;
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> AddCategory (@RequestParam("photo") MultipartFile photo, @Valid @RequestParam String CategoryName){
        if(photo.isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Please upload the file!!!", false), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!photo.getContentType().equals("image/png") && !photo.getContentType().equals("image/jpg") && !photo.getContentType().equals("image/jpeg")){
            return new ResponseEntity<>(new ApiResponse("File is not of image type(JPEG/ JPG or PNG)!!!", false), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        return this.productService.addCategory(CategoryName, photo);
    }
    @GetMapping("/get")
    public ResponseEntity<?> getAllCategories(
            @RequestParam(value ="pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value ="sortBy", defaultValue = "categoryId", required = false) String sortBy,
            @RequestParam(value ="sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        return new ResponseEntity<>(this.productService.getCategories(new PageableDto(pageNumber, pageSize, sortBy, sortDir)), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable(value = "categoryId") Integer categoryId){
        return this.productService.deleteCategoryById(categoryId);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/changeImage/{categoryId}")
    public ResponseEntity<?> changeCategoryImage(@RequestParam("photo") MultipartFile photo, @PathVariable(value = "categoryId") Integer categoryId){
        if (!photo.getContentType().equals("image/png") && !photo.getContentType().equals("image/jpg") && !photo.getContentType().equals("image/jpeg")){
            return new ResponseEntity<>(new ApiResponse("File is not of image type(JPEG/ JPG or PNG)!!!", false), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        return this.productService.changeCategoryImage(photo, categoryId);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/updateCategory/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable("categoryId") Integer categoryId, @RequestBody CategoryDTO categoryDTO){
        return this.productService.updateCategory(categoryId, categoryDTO);
    }
}