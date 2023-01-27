package com.ShopIT.Repository;

import com.ShopIT.Models.MerchantProfile;
import com.ShopIT.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantProfileRepo extends JpaRepository <MerchantProfile, Long> {
    MerchantProfile findByUser(User user);
}
