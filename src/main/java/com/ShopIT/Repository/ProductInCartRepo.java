package com.ShopIT.Repository;

import com.ShopIT.Models.Product;
import com.ShopIT.Models.ProductInCart;
import com.ShopIT.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductInCartRepo extends JpaRepository<ProductInCart, Long> {
    Optional<ProductInCart> findByProductAndUser(Product product, User user);
    Page<ProductInCart> findByUser(User user, Pageable pageable);
}
