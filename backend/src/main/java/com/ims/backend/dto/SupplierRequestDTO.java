package com.ims.backend.dto;

import com.ims.backend.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequestDTO {
    private String name;
    private String contactNumber;
    private String address;

    public Supplier toEntity() {
        Supplier supplier = new Supplier();
        supplier.setName(this.name);
        supplier.setContactNumber(this.contactNumber);
        supplier.setAddress(this.address);
        return supplier;
    }
}
