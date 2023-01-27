package com.ShopIT.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
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
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateOfPurchase;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date created_at;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Address address;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<DeliveryStatus> deliveryStatus = new HashSet<>(0);
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private User user;
    private long amount;
    private String id;
    private String receipt;
    private String currency;
    private String paymentId;
    private String status;
}
