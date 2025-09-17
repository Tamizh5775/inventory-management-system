package com.ims.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contactNumber;
    private String address;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Product> products;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Purchase> purchases;
}
