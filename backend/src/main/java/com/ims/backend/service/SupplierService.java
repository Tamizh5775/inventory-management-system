package com.ims.backend.service;

import com.ims.backend.dto.SupplierDTO;
import com.ims.backend.dto.SupplierRequestDTO;
import java.util.List;

public interface SupplierService {
    List<SupplierDTO> getAllSuppliers();
    SupplierDTO getSupplierById(Long id);
    SupplierDTO saveSupplier(SupplierRequestDTO supplierRequest);
    SupplierDTO updateSupplier(Long id, SupplierRequestDTO supplierRequest);
    void deleteSupplier(Long id);
}
