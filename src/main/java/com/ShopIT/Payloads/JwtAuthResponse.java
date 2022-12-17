package com.ShopIT.Payloads;

import com.ShopIT.Models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {
    private String accessToken;
    private String refreshToken;
    private String firstname;
    private String lastname;
    private Set<Role> roles;
}
