package com.ShopIT.Payloads;

import com.ShopIT.Payloads.Products.DisplayProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecentProductDto {
    private Long Id;
    private List<DisplayProductDto> products;
}
