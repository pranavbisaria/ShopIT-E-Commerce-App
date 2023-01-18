package com.ShopIT.Repository;

import com.ShopIT.Models.Profile;
import com.ShopIT.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, Long> {
    Profile findByUser(User user);
}
