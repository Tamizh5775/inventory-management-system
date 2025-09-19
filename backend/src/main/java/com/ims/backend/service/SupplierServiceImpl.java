package com.ims.backend.service;

import com.ims.backend.dto.SupplierDTO;
import com.ims.backend.dto.SupplierRequestDTO;
import com.ims.backend.exception.SupplierNotFoundException;
import com.ims.backend.model.Supplier;
import com.ims.backend.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(SupplierDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));
        return SupplierDTO.fromEntity(supplier);
    }

    @Override
    public SupplierDTO saveSupplier(SupplierRequestDTO supplierRequest) {
        Supplier supplier = supplierRequest.toEntity();
        Supplier saved = supplierRepository.save(supplier);
        return SupplierDTO.fromEntity(saved);
    }

    @Override
    public SupplierDTO updateSupplier(Long id, SupplierRequestDTO supplierRequest) {
        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));

        existing.setName(supplierRequest.getName());
        existing.setContactNumber(supplierRequest.getContactNumber());
        existing.setAddress(supplierRequest.getAddress());

        Supplier updated = supplierRepository.save(existing);
        return SupplierDTO.fromEntity(updated);
    }

    @Override
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new SupplierNotFoundException(id);
        }
        supplierRepository.deleteById(id);
    }
}
