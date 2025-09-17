package com.ims.backend.dto;

import com.ims.backend.model.Sales;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SalesDTO {
    private Long id;
    private String productName;
    private String customerName;
    private Integer quantitySold;
    private Double totalAmount;
    private LocalDateTime saleDate;

    public static SalesDTO fromEntity(Sales sale) {
        return new SalesDTO(
                sale.getId(),
                sale.getProduct() != null ? sale.getProduct().getName() : null,
                sale.getCustomerName(),
                sale.getQuantitySold(),
                sale.getTotalAmount(),
                sale.getSaleDate()
        );
    }
}
