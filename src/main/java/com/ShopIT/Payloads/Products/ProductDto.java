package com.ShopIT.Payloads.Products;

import com.ShopIT.Models.QuestionModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

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
