package com.ShopIT.Payloads.Products;

import com.ShopIT.Models.QuestionModel;
import com.ShopIT.Models.SpecificationSub;
import com.ShopIT.Models.Sub;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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
    private List<Sub> offers;
    private String warranty;
    private String Highlights;
    private String services;
    private List<SpecificationSub> specification;
    @NotNull
    private List<Sub> description;
    private Set<QuestionModel> questions;
}