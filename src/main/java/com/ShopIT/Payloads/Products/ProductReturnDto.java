package com.ShopIT.Payloads.Products;

import com.ShopIT.Models.*;
import com.ShopIT.Payloads.Categories.CategoryDTO;
import com.ShopIT.Payloads.UserDto;
import com.ShopIT.Payloads.UserShow;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductReturnDto {
    private Long productId;
    @NotEmpty
    private String productName;
    private Set<Images> imageUrls;
    @NotEmpty
    private double originalPrice;
    private double offerPercentage;
    private Integer rating;
    private Long quantityAvailable;
    private String warranty;
    private String offers;
    private String Highlights;
    private String services;
    private String specification;
    @NotEmpty
    private String description;
    private String ratingAndReviews;
    private Long NoOfOrders;
    private Set<QuestionModel> questions;
    private Set<Review> reviews;
    private Set<Category> category = new HashSet<>();
    private UserShow provider;
}
