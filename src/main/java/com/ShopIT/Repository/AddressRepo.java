package com.ShopIT.Repository;

import com.ShopIT.Models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {
}
