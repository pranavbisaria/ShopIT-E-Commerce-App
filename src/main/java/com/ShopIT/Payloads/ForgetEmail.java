package com.ShopIT.Payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
public class ForgetEmail {
    @NotEmpty
    @Email(message = "Email Address is not Valid!!")
    String email;
}
