package com.ims.backend.controller;

import com.ims.backend.dto.SalesDTO;
import com.ims.backend.model.Product;
import com.ims.backend.model.Sales;
import com.ims.backend.repository.ProductRepository;
import com.ims.backend.repository.SalesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SalesController {

    private final SalesRepository salesRepository;
    private final ProductRepository productRepository;

    public SalesController(SalesRepository salesRepository, ProductRepository productRepository) {
        this.salesRepository = salesRepository;
        this.productRepository = productRepository;
    }

    // ------------------- GET all sales -------------------
    @GetMapping
    public List<SalesDTO> getAllSales() {
        return salesRepository.findAll()
                .stream()
                .map(SalesDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ------------------- CREATE sale -------------------
    @PostMapping
    @Transactional
    public ResponseEntity<?> createSale(@RequestBody Sales sale) {
        if (sale.getProduct() == null || sale.getProduct().getId() == null) {
            return ResponseEntity.badRequest().body("Product is required");
        }

        Product product = productRepository.findById(sale.getProduct().getId()).orElse(null);
        if (product == null) {
            return ResponseEntity.status(404).body("Product not found");
        }

        // Decrease stock
        if (product.getQuantity() < sale.getQuantitySold()) {
            return ResponseEntity.badRequest().body("Not enough stock");
        }
        product.setQuantity(product.getQuantity() - sale.getQuantitySold());
        productRepository.save(product);

        // Set total amount and sale date
        sale.setTotalAmount(product.getPrice() * sale.getQuantitySold());
        sale.setSaleDate(LocalDateTime.now());

        Sales savedSale = salesRepository.save(sale);

        return ResponseEntity.ok(SalesDTO.fromEntity(savedSale));
    }

    // ------------------- REPORT: sales between two dates -------------------
    @GetMapping("/report")
    public ResponseEntity<?> getSalesReport(@RequestParam String startDate,
                                            @RequestParam String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
            LocalDateTime end = LocalDateTime.parse(endDate, formatter);

            List<SalesDTO> report = salesRepository
                    .findBySaleDateBetween(start, end)
                    .stream()
                    .map(SalesDTO::fromEntity)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use yyyy-MM-dd'T'HH:mm:ss");
        }
    }
}
