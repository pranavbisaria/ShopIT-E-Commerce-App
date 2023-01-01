package com.ShopIT.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class PageResponse {
    private List<Object> content;
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private long totalElements;
    private boolean lastPage;
}
