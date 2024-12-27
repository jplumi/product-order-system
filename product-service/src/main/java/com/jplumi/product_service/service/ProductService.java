package com.jplumi.product_service.service;

import com.jplumi.product_service.dto.ProductRequest;
import com.jplumi.product_service.dto.ProductResponse;
import com.jplumi.product_service.model.Product;
import com.jplumi.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = new Product(
                null,
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice()
        );

        productRepository.save(product);
        log.info("Product {} is saved.", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        )).toList();
    }

}
