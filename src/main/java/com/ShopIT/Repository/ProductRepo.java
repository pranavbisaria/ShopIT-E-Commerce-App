package com.ShopIT.Repository;
import com.ShopIT.Models.Category;
import com.ShopIT.Models.Product;
import com.ShopIT.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryContaining(Category category, Pageable pageable);
    Page<Product> findAllByProvider(Pageable pageable, User Provider);

    Page<Product> findByCategoryContainingAndProvider(Category category, User Provider, Pageable pageable);
//Customer Side
    @Query(value = "SELECT * FROM product WHERE (lower(product_name) LIKE lower(concat('%',:name,'%'))) AND ((original_price * (100-offer_percentage)/100) BETWEEN :minPrice AND :maxPrice) AND (rating BETWEEN :minRating AND :maxRating) ORDER BY position(lower(:name) in lower(product_name))",
            countQuery = "SELECT count(*) FROM product WHERE (lower(product_name) LIKE lower(concat('%',:name,'%'))) AND ((original_price * (100-offer_percentage)/100) BETWEEN :minPrice AND :maxPrice) AND (rating BETWEEN :minRating AND :maxRating)",
            nativeQuery = true)
    Page<Product> findByProductNameIgnoreCase(@Param("name") String name, Pageable pageable, @Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice, @Param("minRating") double minRating, @Param("maxRating") double maxRating);

    @Query("SELECT p FROM Product p WHERE (lower(p.productName) LIKE lower(concat('%',:name,'%'))) AND ((p.originalPrice * (100-p.offerPercentage)/100) BETWEEN :minPrice AND :maxPrice)  AND (p.rating BETWEEN :minRating AND :maxRating)")
    Page<Product> findByProductNameContainingIgnoreCase(String name, Pageable pageable, @Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice, @Param("minRating") double minRating, @Param("maxRating") double maxRating);

//Merchant Side
    @Query(value = "SELECT * FROM product WHERE (lower(product_name) LIKE lower(concat('%',:name,'%'))) AND ((original_price * (100-offer_percentage)/100) BETWEEN :minPrice AND :maxPrice) AND (rating BETWEEN :minRating AND :maxRating) AND (provider_id = :id) ORDER BY position(lower(:name) in lower(product_name))",
            countQuery = "SELECT count(*) FROM product WHERE (lower(product_name) LIKE lower(concat('%',:name,'%'))) AND ((original_price * (100-offer_percentage)/100) BETWEEN :minPrice AND :maxPrice) AND (rating BETWEEN :minRating AND :maxRating AND (provider_id = :id))",
            nativeQuery = true)
    Page<Product> findByProductNameIgnoreCaseAndProvider(@Param("name") String name, Pageable pageable, @Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice, @Param("minRating") double minRating, @Param("maxRating") double maxRating, @Param("id") long id);

    @Query("SELECT p FROM Product p WHERE (lower(p.productName) LIKE lower(concat('%',:name,'%'))) AND ((p.originalPrice * (100-p.offerPercentage)/100) BETWEEN :minPrice AND :maxPrice)  AND (p.rating BETWEEN :minRating AND :maxRating) AND p.provider = :provider")
    Page<Product> findByProductNameContainingIgnoreCaseAndProvider(String name, Pageable pageable, @Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice, @Param("minRating") double minRating, @Param("maxRating") double maxRating, @Param("provider") User provider);
}
//provider_id