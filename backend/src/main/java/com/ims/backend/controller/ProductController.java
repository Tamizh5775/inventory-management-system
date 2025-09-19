package com.ims.backend.controller;

import com.ims.backend.dto.ProductDTO;
import com.ims.backend.dto.ProductRequestDTO;
import com.ims.backend.model.Product;
import com.ims.backend.model.Supplier;
import com.ims.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductRequestDTO dto) {
        Product savedProduct = productService.saveProduct(dto);
        return ResponseEntity
                .created(URI.create("/api/products/" + savedProduct.getId()))
                .body(ProductDTO.fromEntity(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
                                                    @Valid @RequestBody ProductRequestDTO dto) {
        return productService.updateProduct(id, dto)
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

    // ------------------- Helper method -------------------
    private Product mapToEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        if (dto.getSupplierId() != null) {
            Supplier supplier = new Supplier();
            supplier.setId(dto.getSupplierId());
            product.setSupplier(supplier);
        }
        return product;
    }
}
