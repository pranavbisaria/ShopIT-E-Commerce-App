package com.ShopIT.Models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Images> imageUrls = new ArrayList<>(0);
    private String productName;
    private double originalPrice;
    private double offerPercentage;
    private Integer rating;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sub> offers = new ArrayList<>(0);
    private Long quantityAvailable;
    private String warranty;
    private Long quantityAllowedPerUser=5L;
    private String Highlights;
    @Column(length = 10000)
    private String services;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sub> specification = new ArrayList<>(0);
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sub> description = new ArrayList<>(0);
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private User provider;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<User> customer = new HashSet<>(0);
    private Long NoOfOrders = 0L;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>(0);
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<QuestionModel> questions = new HashSet<>(0);
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Category> category = new HashSet<>(0);
}
