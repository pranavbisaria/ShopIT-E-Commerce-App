package com.ShopIT.Repository;

import com.ShopIT.Models.Cart;
import com.ShopIT.Models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    Cart findByProfile(Profile profile);
}
