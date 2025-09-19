package com.ims.backend.service;

import com.ims.backend.dto.ProductRequestDTO;
import com.ims.backend.model.Product;
import com.ims.backend.model.Supplier;
import com.ims.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(ProductRequestDTO dto) {
        Product product = mapToEntity(dto);
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> updateProduct(Long id, ProductRequestDTO dto) {
        return productRepository.findById(id).map(existing -> {
            existing.setName(dto.getName());
            existing.setCategory(dto.getCategory());
            existing.setPrice(dto.getPrice());
            existing.setQuantity(dto.getQuantity());

            if (dto.getSupplierId() != null) {
                Supplier supplier = new Supplier();
                supplier.setId(dto.getSupplierId());
                existing.setSupplier(supplier);
            } else {
                existing.setSupplier(null);
            }

            return productRepository.save(existing);
        });
    }

    @Override
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findByQuantityLessThan(threshold);
    }

    // ------------------- Helper -------------------
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
