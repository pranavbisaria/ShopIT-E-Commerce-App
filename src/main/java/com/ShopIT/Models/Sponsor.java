package com.ShopIT.Models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Entity
public class Sponsor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Images> images = new ArrayList<>(0);
}
