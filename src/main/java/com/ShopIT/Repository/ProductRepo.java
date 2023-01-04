package com.ShopIT.Repository;
import com.ShopIT.Models.Category;
import com.ShopIT.Models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryContaining(Category category, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY p.productName ASC, p.Highlights DESC, p.services ASC, p.specification DESC, p.description ASC")
    List<Product> findAll(@Param("keyword") String keyword);
}
