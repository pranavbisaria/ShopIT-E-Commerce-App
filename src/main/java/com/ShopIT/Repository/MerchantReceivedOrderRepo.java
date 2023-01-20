package com.ShopIT.Repository;

import com.ShopIT.Models.MerchantOrderReceived;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantReceivedOrderRepo  extends JpaRepository<MerchantOrderReceived, Long> {
}
