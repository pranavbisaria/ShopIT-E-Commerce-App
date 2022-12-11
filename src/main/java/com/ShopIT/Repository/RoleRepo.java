package com.ShopIT.Repository;

import com.ShopIT.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository <Role, Integer>{
}
