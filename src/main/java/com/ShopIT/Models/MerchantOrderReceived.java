package com.ShopIT.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @ManyToOne()
    private Address address;
    @ManyToOne
    private Product product;
    private Long Quantity;
    private String paymentId;
    private Integer amountReceived;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateOfOrder;
}
