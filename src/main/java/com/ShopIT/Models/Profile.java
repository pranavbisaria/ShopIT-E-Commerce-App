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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Address> address = new HashSet<>(0);
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    private Cart cart = new Cart();
    @OneToMany(mappedBy = "profiles", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MyOrders> myOrders = new HashSet<>(0);
    @OneToMany(mappedBy = "profiles", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> myReviews = new HashSet<>(0);
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    private WishList wishList;
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    private RecentProduct recentProduct = new RecentProduct();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "profile_user",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;
}
