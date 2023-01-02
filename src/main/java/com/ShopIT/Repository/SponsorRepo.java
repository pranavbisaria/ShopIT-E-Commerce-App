package com.ShopIT.Repository;

import com.ShopIT.Models.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorRepo extends JpaRepository<Sponsor, Integer> {
}
