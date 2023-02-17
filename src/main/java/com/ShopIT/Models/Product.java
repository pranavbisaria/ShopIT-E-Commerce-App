package com.ShopIT.Models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AllArgsConstructor
@Getter@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Images> imageUrls = new ArrayList<>(0);
    private String productName;
    private double originalPrice;
    private double offerPercentage;
    private float rating = 0f;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sub> offers = new ArrayList<>(0);
    private Long quantityAvailable;
    private String warranty;
    private Long quantityAllowedPerUser=5L;
    private String Highlights;
    @Column(length = 10000)
    private String services;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SpecificationSub> specification = new ArrayList<>(0);
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Sub> description = new ArrayList<>(0);
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIgnore
    private User provider;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<User> customer = new HashSet<>(0);
    private Long NoOfOrders = 0L;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>(0);
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<QuestionModel> questions = new HashSet<>(0);
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Category> category = new HashSet<>(0);
}
