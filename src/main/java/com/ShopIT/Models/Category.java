package com.ShopIT.Models;//package com.ShopIT.Models;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import jakarta.persistence.*;
//import java.util.List;
//@Getter@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//public class Category {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private int categoryId;
//    @Column(nullable = false)
//    private String categoryName;
//    private String imageName;
//    @OneToMany(cascade = CascadeType.PERSIST)
//    private List<Product> internshipsList;
//}