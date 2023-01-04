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
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Address> address = new HashSet<>(0);
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Cart cart = new Cart();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<MyOrders> myOrders = new HashSet<>(0);
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Review> myReviews = new HashSet<>(0);
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private WishList wishList = new WishList();
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<Notification> notifications;
}
