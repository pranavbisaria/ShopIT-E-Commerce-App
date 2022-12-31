package com.ShopIT.Repository;
import com.ShopIT.Models.Category;
import com.ShopIT.Models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryContaining(Category category, Pageable pageable);
}
