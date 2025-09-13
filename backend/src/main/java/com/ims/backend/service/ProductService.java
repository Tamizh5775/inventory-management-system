package com.ims.backend.service;

import com.ims.backend.model.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product saveProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    List<Product> searchProducts(String keyword);
    List<Product> getLowStockProducts(int threshold);
}
