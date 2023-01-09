package com.ShopIT.Payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrder {
    private Long amount;
    private Long amount_paid;
    private String[] notes;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date created_at;
    private Long amount_due;
    private String currency;
    private String receipt;
    private String id;
    private String entity;
    private Long offer_id;
    private String status;
    private Integer attempts;
}
