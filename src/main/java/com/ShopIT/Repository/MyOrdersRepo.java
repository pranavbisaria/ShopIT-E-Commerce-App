package com.ShopIT.Repository;

import com.ShopIT.Models.MyOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyOrdersRepo extends JpaRepository<MyOrders, Integer> {
    Optional<MyOrders> findById(String orderId);
}
