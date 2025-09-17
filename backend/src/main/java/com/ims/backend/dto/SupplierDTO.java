package com.ims.backend.dto;

import com.ims.backend.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupplierDTO {
    private Long id;
    private String name;
    private String contactNumber;
    private String address;

    public static SupplierDTO fromEntity(Supplier supplier) {
        return new SupplierDTO(
                supplier.getId(),
                supplier.getName(),
                supplier.getContactNumber(),
                supplier.getAddress()
        );
    }
}
