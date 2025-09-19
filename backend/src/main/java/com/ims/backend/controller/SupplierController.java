package com.ims.backend.controller;

import com.ims.backend.dto.SupplierDTO;
import com.ims.backend.dto.SupplierRequestDTO;
import com.ims.backend.service.SupplierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // ------------------- GET all suppliers -------------------
    @GetMapping
    public List<SupplierDTO> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    // ------------------- GET supplier by ID -------------------
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    // ------------------- CREATE new supplier -------------------
    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(@RequestBody SupplierRequestDTO supplierRequest) {
        return ResponseEntity.ok(supplierService.saveSupplier(supplierRequest));
    }

    // ------------------- UPDATE supplier -------------------
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Long id,
                                                      @RequestBody SupplierRequestDTO supplierRequest) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, supplierRequest));
    }

    // ------------------- DELETE supplier -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok("Supplier deleted successfully");
    }
}
