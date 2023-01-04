package com.ShopIT.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeliveryStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    private Date date;
    private String status;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Product> product = new HashSet<>(0);
}