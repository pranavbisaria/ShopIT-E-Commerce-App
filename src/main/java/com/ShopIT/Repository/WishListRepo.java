package com.ShopIT.Repository;
import com.ShopIT.Models.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface WishListRepo extends JpaRepository<WishList, Long> {
}
