package com.ShopIT.Repository;

import com.ShopIT.Models.MerchantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantProfileRepo extends JpaRepository <MerchantProfile, Long> {
}
