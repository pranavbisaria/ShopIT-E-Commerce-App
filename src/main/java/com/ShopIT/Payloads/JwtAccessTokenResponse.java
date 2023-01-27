package com.ShopIT.Payloads;

import com.ShopIT.Models.Role;
import java.util.Set;

public record JwtAccessTokenResponse (
    String accessToken,
    String firstname,
    String lastname,
    String Email,
    Set<Role> roles
){}
