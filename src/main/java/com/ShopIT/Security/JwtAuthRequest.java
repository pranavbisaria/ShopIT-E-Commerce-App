package com.ShopIT.Security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JwtAuthRequest {
    @Email
    private String email;
    @Size(min = 8, message = "{\"Password\":\"Must be of minimum 8 characters\"}")
    private String password;
}

