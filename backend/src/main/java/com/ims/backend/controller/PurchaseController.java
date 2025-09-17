package com.ims.backend.controller;

import com.ims.backend.dto.PurchaseDTO;
import com.ims.backend.model.Product;
import com.ims.backend.model.Purchase;
import com.ims.backend.repository.ProductRepository;
import com.ims.backend.repository.PurchaseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin(origins = "*")
public class PurchaseController {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;

    public PurchaseController(PurchaseRepository purchaseRepository, ProductRepository productRepository) {
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
    }

    // ------------------- GET all purchases -------------------
    @GetMapping
    public List<PurchaseDTO> getAllPurchases() {
        return purchaseRepository.findAll()
                .stream()
                .map(PurchaseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ------------------- CREATE purchase -------------------
    @PostMapping
    @Transactional
    public ResponseEntity<?> createPurchase(@RequestBody Purchase purchase) {
        if (purchase.getProduct() == null || purchase.getProduct().getId() == null) {
            return ResponseEntity.badRequest().body("Product is required");
        }

        Product product = productRepository.findById(purchase.getProduct().getId()).orElse(null);
        if (product == null) {
            return ResponseEntity.status(404).body("Product not found");
        }

        // Increase stock
        product.setQuantity(product.getQuantity() + purchase.getQuantityPurchased());
        productRepository.save(product);

        // Set total cost and purchase date
        purchase.setTotalCost(product.getPrice() * purchase.getQuantityPurchased());
        purchase.setPurchaseDate(LocalDateTime.now());

        // Save purchase
        Purchase savedPurchase = purchaseRepository.save(purchase);

        return ResponseEntity.ok(PurchaseDTO.fromEntity(savedPurchase));
    }

    // ------------------- REPORT: purchases between two dates -------------------
    @GetMapping("/report")
    public ResponseEntity<?> getPurchaseReport(@RequestParam String startDate,
                                               @RequestParam String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
            LocalDateTime end = LocalDateTime.parse(endDate, formatter);

            List<PurchaseDTO> report = purchaseRepository
                    .findByPurchaseDateBetween(start, end)
                    .stream()
                    .map(PurchaseDTO::fromEntity)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use yyyy-MM-dd'T'HH:mm:ss");
        }
    }
}
