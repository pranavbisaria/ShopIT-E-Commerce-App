package com.ShopIT.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MerchantOrderReceived {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Product product;
    private Long Quantity;
    private String paymentId;
    private Integer amountReceived;
    @JsonFormat(pattern = "dd-MMM-yyyy")
    private Date dateOfOrder;
    @ManyToOne(fetch = FetchType.EAGER)
    private User Customer;
}
