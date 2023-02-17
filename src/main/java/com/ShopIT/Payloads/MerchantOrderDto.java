package com.ShopIT.Payloads;

import com.ShopIT.Models.Address;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.Products.ProductNameDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class MerchantOrderDto {
    private Long Id;
    private Address address;
    private ProductNameDto product;
    private Long Quantity;
    private String paymentId;
    private Integer amountReceived;
    private Date dateOfOrder;
    private UserShow Customer;
}
