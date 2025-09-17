package com.ims.backend.dto;

import com.ims.backend.model.Purchase;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PurchaseDTO {
    private Long id;
    private String productName;
    private String supplierName;
    private Integer quantityPurchased;
    private Double totalCost;
    private LocalDateTime purchaseDate;

    public static PurchaseDTO fromEntity(Purchase purchase) {
        return new PurchaseDTO(
                purchase.getId(),
                purchase.getProduct() != null ? purchase.getProduct().getName() : null,
                purchase.getSupplier() != null ? purchase.getSupplier().getName() : null,
                purchase.getQuantityPurchased(),
                purchase.getTotalCost(),
                purchase.getPurchaseDate()
        );
    }
}

