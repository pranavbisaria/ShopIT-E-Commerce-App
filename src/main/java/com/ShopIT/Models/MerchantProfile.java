package com.ShopIT.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
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
    private Set<Address> address = new HashSet<>(0);
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Cart cart = new Cart();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<MyOrders> paymentsReceived = new HashSet<>(0);
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Product> products = new HashSet<>(0);
//    @ManyToMany
//    private Set<Notification> notifications;
}
