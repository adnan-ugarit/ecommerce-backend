package com.adnan.catalog.mappers;

import com.adnan.catalog.dto.ProductDto;
import com.adnan.catalog.entities.Category;
import com.adnan.catalog.entities.Product;

public class ProductMapper {
    
    public static ProductDto getDtoFromProduct(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setImageUrl(product.getImageUrl());
        productDto.setPrice(product.getPrice());
        productDto.setInStock(product.isInStock());
        productDto.setCategoryId(product.getCategory().getId());
        return productDto;
    }
    
    public static Product getProductFromDto(ProductDto productDto, Category category) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setImageUrl(productDto.getImageUrl());
        product.setPrice(productDto.getPrice());
        product.setInStock(productDto.isInStock());
        product.setCategory(category);
        return product;
    }
    
}
