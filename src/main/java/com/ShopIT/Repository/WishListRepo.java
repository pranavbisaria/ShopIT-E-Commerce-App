package com.ShopIT.Repository;
import com.ShopIT.Models.Profile;
import com.ShopIT.Models.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface WishListRepo extends JpaRepository<WishList, Long> {
    WishList findByProfile(Profile profile);
}
