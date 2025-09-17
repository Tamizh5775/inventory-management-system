package com.ims.backend.service;

import com.ims.backend.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product saveProduct(Product product);
    Optional<Product> updateProduct(Long id, Product product);
    boolean deleteProduct(Long id);
    List<Product> searchProducts(String keyword);
    List<Product> getLowStockProducts(int threshold);
}
