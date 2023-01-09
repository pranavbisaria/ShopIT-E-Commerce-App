package com.ShopIT.Models;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Images> imageUrls = new LinkedHashSet<>(0);
    private String productName;
    private double originalPrice;
    private double offerPercentage;
    private Integer rating;
    @Column(length = 10000)
    private String offers;
    private Long quantityAvailable;
    private String warranty;
    private Long quantityAllowedPerUser=5L;
    @Column(length = 10000)
    private String Highlights;
    @Column(length = 10000)
    private String services;
    @Column(length = 10000)
    private String specification;
    @Column(length = 10000)
    private String description;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private User provider;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<User> customer = new HashSet<>(0);
    private Long NoOfOrders = 0L;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>(0);
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<QuestionModel> questions = new HashSet<>(0);
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Category> category = new HashSet<>(0);
}
