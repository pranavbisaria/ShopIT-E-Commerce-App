package com.ShopIT.Repository;
import com.ShopIT.Models.Profile;
import com.ShopIT.Models.RecentProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentProductRepo extends JpaRepository <RecentProduct, Long> {
    RecentProduct findByProfile(Profile profile);
}
