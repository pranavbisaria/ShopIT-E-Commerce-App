package com.ShopIT.Payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private Long Id;
    private String profilePhoto;
    @NotEmpty(message = "First name can't be empty")
    private String firstname;
    private String lastname;
    @Email(message = "Invalid Email")
    private String email;
    private String gender;
    @Pattern(regexp="(^$|[0-9]{10})", message = "Phone number must be 10 digit long")
    private String phoneNumber;
}
