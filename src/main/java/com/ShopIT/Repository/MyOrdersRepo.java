package com.ShopIT.Repository;

import com.ShopIT.Models.MyOrders;
import com.ShopIT.Models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyOrdersRepo extends JpaRepository<MyOrders, Integer> {
    Optional<MyOrders> findByOrderId(String orderId);
    Page<MyOrders> findByProfiles(Profile profile, Pageable pageable);
}
