package com.ShopIT.Repository;

import com.ShopIT.Models.Product;
import java.util.List;

public interface ProductCustomRepo {
    List<Product> searchProduct(String keyword);
}
