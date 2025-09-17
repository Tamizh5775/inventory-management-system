package com.ims.backend.controller;

import com.ims.backend.dto.SupplierDTO;
import com.ims.backend.model.Supplier;
import com.ims.backend.repository.SupplierRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
public class SupplierController {

    private final SupplierRepository supplierRepository;

    public SupplierController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    // ------------------- GET all suppliers -------------------
    @GetMapping
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(SupplierDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ------------------- GET supplier by ID -------------------
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable Long id) {
        return supplierRepository.findById(id)
                .map(SupplierDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // ------------------- CREATE new supplier -------------------
    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(@RequestBody Supplier supplier) {
        Supplier savedSupplier = supplierRepository.save(supplier);
        return ResponseEntity.ok(SupplierDTO.fromEntity(savedSupplier));
    }

    // ------------------- UPDATE supplier -------------------
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        Optional<Supplier> updatedSupplier = supplierRepository.findById(id)
                .map(existing -> {
                    existing.setName(supplier.getName());
                    existing.setContactNumber(supplier.getContactNumber());
                    existing.setAddress(supplier.getAddress());
                    return supplierRepository.save(existing);
                });
        return updatedSupplier
                .map(s -> ResponseEntity.ok(SupplierDTO.fromEntity(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------- DELETE supplier -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long id) {
        if (supplierRepository.existsById(id)) {
            supplierRepository.deleteById(id);
            return ResponseEntity.ok("Supplier deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
