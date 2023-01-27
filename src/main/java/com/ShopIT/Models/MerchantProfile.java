package com.ShopIT.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MerchantProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Address> address = new ArrayList<>(0);
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Cart cart = new Cart();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MerchantOrderReceived> paymentsReceived = new ArrayList<>(0);
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>(0);
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}
