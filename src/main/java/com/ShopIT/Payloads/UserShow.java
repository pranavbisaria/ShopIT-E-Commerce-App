package com.ShopIT.Payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserShow {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String profilePhoto;
}
