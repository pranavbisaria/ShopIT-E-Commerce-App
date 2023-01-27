package com.ShopIT.Payloads.Products;

import com.ShopIT.Models.*;
import com.ShopIT.Payloads.Categories.CategoryDTO;
import com.ShopIT.Payloads.UserDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.UUID;

@AllArgsConstructor
@Getter@Setter
@NoArgsConstructor
public class ProductDto {
    private Long Id;
    @NotNull
    private String productName;
    @NotNull
    private double originalPrice;
    private double offerPercentage;
    private Long quantityAvailable;
    private String offers;
    private String warranty;
    private String Highlights;
    private String services;
    private String specification;
    @NotNull
    private String description;
    private Set<QuestionModel> questions;
}
