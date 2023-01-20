package com.ShopIT.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class MyOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateOfPurchase;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date created_at;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DeliveryStatus> deliveryStatus = new ArrayList<>(0);
    private Integer amount;
    private String orderId;
    private String receipt;
    private String currency;
    private String paymentId;
    private String status;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Profile profiles;
}
