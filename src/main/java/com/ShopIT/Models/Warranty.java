package com.ShopIT.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Setter
@Getter@NoArgsConstructor
@AllArgsConstructor
public class Warranty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String Id;
    private String type;
    private String coveredInWarranty;
    private String notCoveredInWarranty;
    private String provider;
    private String providerSite;
    private String providerContacts;
}
