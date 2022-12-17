package com.ShopIT.Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoogleSignModel {
    private String email;
    private String picture;
    private String given_name;
    private String family_name;
    private String iss;
    private String azp;
    private Long exp;
}
