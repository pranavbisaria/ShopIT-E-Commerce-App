package com.ShopIT.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @OneToMany
    private Set<Address> address;
//    @OneToOne
//    private Cart cart;
//    @OneToMany
//    private Set<Offer> order;
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
