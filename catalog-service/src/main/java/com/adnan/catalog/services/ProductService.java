package com.adnan.catalog.services;

import com.adnan.catalog.dto.ProductDto;
import com.adnan.catalog.entities.Category;
import com.adnan.catalog.entities.Product;
import com.adnan.catalog.mappers.ProductMapper;
import com.adnan.catalog.repositories.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = ProductMapper.getDtoFromProduct(product);
            productDtos.add(productDto);
        }
        return productDtos;
    }
    
    public List<ProductDto> getAvailableProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            if (product.isInStock()) {
                ProductDto productDto = ProductMapper.getDtoFromProduct(product);
                productDtos.add(productDto);
            }
        }
        return productDtos;
    }
    
    public Optional<Product> findProductById(Long productId) {
        return productRepository.findById(productId);
    }
    
    public void addProduct(ProductDto productDto, Category category) {
        Product product = ProductMapper.getProductFromDto(productDto, category);
        productRepository.save(product);
    }
    
    public void updateProduct(Long productId, ProductDto productDto, Category category) {
        Product product = ProductMapper.getProductFromDto(productDto, category);
        product.setId(productId);
        productRepository.save(product);
    }
    
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
    
}
