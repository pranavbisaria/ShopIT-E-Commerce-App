package com.ShopIT.Models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @OneToMany
    private Set<Address> address;
    private String phoneNumber;
//    @OneToMany
//    private Set<Order> order;
//    @OneToMany
//    private Set<Payments> payments;
//    private String coupons;
//    @ManyToMany
//    private Set<Product> products;
//    @OneToMany
//    private Set<Review> reviews;
//    @ManyToMany
//    private Set<Notification> notifications;
}
