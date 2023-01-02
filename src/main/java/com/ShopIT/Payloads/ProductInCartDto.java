package com.ShopIT.Payloads;

import com.ShopIT.Payloads.Products.DisplayProductDto;
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
    private Date dateOfOrder;
    private Long noOfProducts;
    private UserShow user;
}
