package com.ShopIT.Payloads;
import com.ShopIT.Payloads.Products.DisplayProductDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInCartDto {
    private Long Id;
    private DisplayProductDto product;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateOfOrder;
    private Long noOfProducts;
    private Boolean available;
}
