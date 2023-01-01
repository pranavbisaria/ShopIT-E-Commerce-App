package com.ShopIT.Models;

import jakarta.persistence.*;

import java.util.Date;
@Entity
public class productInCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @OneToOne
    private Product product;
    private Date dateOfOrder;
    private Long NoOfPurchase;
    @OneToOne
    private User user;
}
