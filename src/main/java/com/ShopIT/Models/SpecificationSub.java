package com.ShopIT.Models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
@Entity
@NoArgsConstructor
@Getter@Setter
@AllArgsConstructor
public class SpecificationSub {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 10000)
    private String head;
    @Column(length = 10000)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Sub> body;
}
