package com.ShopIT.Payloads.Products;
import com.ShopIT.Models.*;
import com.ShopIT.Payloads.UserShow;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductReturnDto {
    private Long productId;
    @NotEmpty
    private String productName;
    private List<Images> imageUrls;
    @NotEmpty
    private double originalPrice;
    private double offerPercentage;
    private Integer rating;
    private Long quantityAvailable;
    private String warranty;
    private List<Sub> offers;
    private String Highlights;
    private String services;
    private List<Sub> specification;
    @NotEmpty
    private List<Sub> description;
    private String ratingAndReviews;
    private Long NoOfOrders;
    private Set<QuestionModel> questions;
    private List<Review> reviews;
    private Set<Category> category = new HashSet<>();
    private UserShow provider;
}
