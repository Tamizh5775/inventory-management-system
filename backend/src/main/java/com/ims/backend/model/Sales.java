package com.ims.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sales")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private Integer quantitySold;
    private Double totalAmount;
    private LocalDateTime saleDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference("product-sales")
    private Product product;
}
