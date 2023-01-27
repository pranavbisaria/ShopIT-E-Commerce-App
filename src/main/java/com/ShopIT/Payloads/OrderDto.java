package com.ShopIT.Payloads;

import com.ShopIT.Models.Address;
import com.ShopIT.Models.DeliveryStatus;
import com.ShopIT.Models.Profile;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String orderId;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateOfPurchase;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date created_at;
    private Set<DeliveryStatus> deliveryStatus = new HashSet<>(0);
    private Integer amount;
    private String paymentId;
    private String status;
}
