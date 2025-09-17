package com.ims.backend.controller;

import com.ims.backend.dto.ProductDTO;
import com.ims.backend.model.Product;
import com.ims.backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ------------------- GET all products -------------------
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts()
                .stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ------------------- GET product by ID -------------------
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ProductDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------- CREATE new product -------------------
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(ProductDTO.fromEntity(savedProduct));
    }

    // ------------------- UPDATE product -------------------
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product)
                .map(ProductDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ------------------- DELETE product -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Product deleted successfully");
    }

    // ------------------- SEARCH products -------------------
    @GetMapping("/search")
    public List<ProductDTO> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword)
                .stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ------------------- LOW STOCK products -------------------
    @GetMapping("/low-stock")
    public List<ProductDTO> getLowStockProducts(@RequestParam(defaultValue = "5") int threshold) {
        return productService.getLowStockProducts(threshold)
                .stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
