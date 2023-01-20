package com.ShopIT.Repository;

import com.ShopIT.Models.Profile;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.UserShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    Boolean existsUserByPhoneNumber(String phoneNumber);
    User findByProfile(Profile profile);
}
