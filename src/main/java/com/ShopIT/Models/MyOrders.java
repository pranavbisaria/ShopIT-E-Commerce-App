package com.ShopIT.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MyOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orderId;
    private Date dateOfPurchase;
    private Date created_at;
    @OneToMany
    private List<DeliveryStatus> deliveryStatus;
    @ManyToOne
    private User user;
    private long amount;
    private String id;
    private String receipt;
    private String currency;
    private String paymentId;
    private String status;
}