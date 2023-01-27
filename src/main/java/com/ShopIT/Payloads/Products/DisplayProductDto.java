package com.ShopIT.Payloads.Products;
import com.ShopIT.Models.Images;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DisplayProductDto {
    private Long productId;
    private Set<Images> imageUrls;
    private String productName;
    private double originalPrice;
    private double offerPercentage;
    private Long quantityAvailable;
    private Integer rating;
}
