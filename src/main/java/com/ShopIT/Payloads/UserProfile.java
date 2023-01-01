package com.ShopIT.Payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(min = 10, max = 10)
    @Pattern(regexp="(^$|[0-9]{10})", message = "Phone number must be 10 digit long")
    private String phoneNumber;
}
