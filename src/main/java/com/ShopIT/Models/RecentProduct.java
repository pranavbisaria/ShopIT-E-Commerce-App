package com.ShopIT.Models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RecentProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>(0);
    @OneToOne(cascade = CascadeType.ALL)
    private Profile profile;
}
