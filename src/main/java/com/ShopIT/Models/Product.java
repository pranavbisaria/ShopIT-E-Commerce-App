//package com.ShopIT.Models;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.aspectj.weaver.patterns.TypePatternQuestions;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Set;
//
//@Entity
//@Getter
//@Setter@AllArgsConstructor
//@NoArgsConstructor
//public class Product {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private String productId;
//    private String productName;
//    private double originalPrice;
//    private double offerPrice;
//    private Integer rating;
//    private String easyPaymentOptions;
//    @ManyToMany
//    private List<Offer> offers;
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<Integer> pincodeAvailForDelivery;
//    private Warranty warranty;
//    private String Highlights;
//    private String services;
//    @ManyToOne
//    private Set<User> provider;
//    private String description;
//    private Set<Specification> specification;
//    private String ratingAndReviews;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<QuestionModel> questions;
//}
