package com.ShopIT.Payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterMerchant {
    private Integer id;
    @NotEmpty
    @Email(message = "Email Address is not Valid!!")
    private String companyEmail;
    @NotEmpty
    @Size(min = 8, message = "Password must be minimum of 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must Contain at least one uppercase letter, one lowercase letter, one numeric character, one special character and no spaces")
    private String password;
}
