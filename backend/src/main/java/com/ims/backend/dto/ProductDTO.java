package com.ims.backend.dto;

import com.ims.backend.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private Integer quantity;
    private String supplierName;

    public static ProductDTO fromEntity(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getQuantity(),
                product.getSupplier() != null ? product.getSupplier().getName() : null
        );
    }
}
