package com.ShopIT.Payloads;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String head;
    private String body;
    private String image;
    private String Id;
}
