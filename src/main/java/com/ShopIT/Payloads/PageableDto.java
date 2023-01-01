package com.ShopIT.Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageableDto {
    private Integer pageNumber;
    private Integer pageSize;
    private String sortBy;
    private String sortDir;
}
