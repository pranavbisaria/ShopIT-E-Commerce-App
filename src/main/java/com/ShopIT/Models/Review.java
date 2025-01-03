package com.ShopIT.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter@Getter
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Images> images = new HashSet<>(0);
    private Integer rating = null;
    @Column(length = 10000)
    private String description;
    private Date issueTime;
    @ManyToOne(cascade = CascadeType.ALL)
    private Profile profiles;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_product_id")
    private Product product;
}
