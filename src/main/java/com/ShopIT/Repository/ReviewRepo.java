package com.ShopIT.Repository;

import com.ShopIT.Models.Product;
import com.ShopIT.Models.Profile;
import com.ShopIT.Models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
    Boolean existsByProfilesAndProduct(Profile profile, Product product);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.rating = :rating and r.product = :product")
    long countByRatingAndProductId(@Param("rating") int rating, @Param("product") Product product);
    Page<Review> findAllByProduct (Product product, Pageable pageable);
    Page<Review> findAllByProfiles (Profile profile, Pageable pageable);
    Review findByProductAndProfiles (Product product, Profile profile);
    long countByProduct(Product product);
}
