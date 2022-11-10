package com.adnan.catalog.controllers;

import com.adnan.catalog.dto.ExternalProductDto;
import com.adnan.catalog.dto.ProductDto;
import com.adnan.catalog.entities.Category;
import com.adnan.catalog.entities.Product;
import com.adnan.catalog.mappers.ProductMapper;
import com.adnan.catalog.metrics.TPMCounter;
import com.adnan.catalog.services.CategoryService;
import com.adnan.catalog.services.ExternalApiService;
import com.adnan.catalog.services.ProductService;
import com.adnan.catalog.utils.FileUtil;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
//import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ExternalApiService externalApiService;
    
    @Autowired
    private TPMCounter tpm;
    
    private final MeterRegistry meterRegistry;
    
    /*@Autowired
    private GaugeService gaugeService;*/
    
    private final AtomicInteger tpmGauge;
    
    public ProductController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        tpmGauge = this.meterRegistry.gauge("gauge.tpm", new AtomicInteger(0));
    }
    
    @GetMapping("")
    public ResponseEntity<List<ProductDto>> getAvailableProducts() {
        LOGGER.info("Finding available products");
        List<ProductDto> body = productService.getAvailableProducts();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    
    @GetMapping("external")
    public ResponseEntity<List<ExternalProductDto>> getExternalProducts() {
        LOGGER.info("Finding all external products");
        List<ExternalProductDto> body = externalApiService.consumeProducts();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable("productId") Long productId) {
        int count = tpm.increment();
        //gaugeService.submit("tpm", count);
        if (tpmGauge != null) {
            tpmGauge.set(count);
        }
        LOGGER.info("Finding product by id: {}", productId);
        Optional<Product> optionalProduct = productService.findProductById(productId);
        if (!optionalProduct.isPresent()) {
            LOGGER.info("Product (id = {}) is not found", productId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        Product product = optionalProduct.get();
        ProductDto body = ProductMapper.getDtoFromProduct(product);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<ProductDto>> findProductsByCategoryName(@PathVariable("categoryName") String categoryName) {
        LOGGER.info("Finding products by category name: {}", categoryName);
        Optional<Category> optionalCategory = categoryService.findCategoryByName(categoryName);
        if (!optionalCategory.isPresent()) {
            LOGGER.error("Category (name = {}) is not found", categoryName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Category category = optionalCategory.get();
        LOGGER.info("Finding available products");
        List<ProductDto> products = productService.getAvailableProducts();
        List<ProductDto> body = products.stream()
                .filter(p -> Objects.equals(p.getCategoryId(), category.getId()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    
    @GetMapping("/uploaded-images/products/{fileName:.+}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName) {
        String downloadDir = "uploaded-images/products";
        LOGGER.info("Loading product image");
        Resource file = FileUtil.loadFile(downloadDir, fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
    
}
