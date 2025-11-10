/* @MENTEE_POWER (C)2025 */
package ru.mentee.power.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.api.generated.dto.ProductCreateRequest;
import ru.mentee.power.api.generated.dto.ProductPageResponse;
import ru.mentee.power.api.generated.dto.ProductResponse;
import ru.mentee.power.api.generated.dto.ProductUpdateRequest;
import ru.mentee.power.domain.exception.ProductNotFoundException;
import ru.mentee.power.domain.mapper.ProductMapper;
import ru.mentee.power.domain.model.Product;
import ru.mentee.power.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse createProduct(ProductCreateRequest createRequest) {
        log.info("Creating product with name: {}", createRequest.getName());
        Product product = productMapper.toProduct(createRequest);
        Product savedProduct = productRepository.save(product);
        log.info("Product created with id: {}", savedProduct.getId());

        return productMapper.toProductResponse(savedProduct);
    }

    public void deleteProductById(UUID id) {

        log.info("Deleting product with id: {}", id);

        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ProductNotFoundException(
                                                "Product not found with id: " + id));

        productRepository.delete(product);
        log.info("Product deleted with id: {}", id);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID id) {
        log.debug("Fetching product with id: {}", id);

        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ProductNotFoundException(
                                                "Product not found with id: " + id));

        return productMapper.toProductResponse(product);
    }

    public ProductResponse updateProduct(UUID id, ProductUpdateRequest updateRequest) {
        log.info("Updating product with id: {}", id);

        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ProductNotFoundException(
                                                "Product not found with id: " + id));
        Product updatedProduct =
                productRepository.save(
                        productMapper.updateProductFromRequest(product, updateRequest));

        log.info("Product updated with id: {}", updatedProduct.getId());
        return productMapper.toProductResponse(updatedProduct);
    }

    @Transactional(readOnly = true)
    public ProductPageResponse getAllProducts(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean inStock,
            Pageable pageable) {
        log.debug(
                "Fetching products with filters - name: {}, minPrice: {}, maxPrice: {}, inStock: {}",
                name,
                minPrice,
                maxPrice,
                inStock);

        Page<Product> productPage;

        if (hasFilters(name, minPrice, maxPrice, inStock)) {
            productPage =
                    productRepository.findAllWithFilters(
                            name, minPrice, maxPrice, inStock, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }
        return buildProductPageResponse(productPage);
    }

    private boolean hasFilters(
            String name, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock) {
        return name != null || minPrice != null || maxPrice != null || inStock != null;
    }

    private ProductPageResponse buildProductPageResponse(Page<Product> productPage) {
        List<ProductResponse> content =
                productPage.getContent().stream().map(productMapper::toProductResponse).toList();

        ProductPageResponse response = new ProductPageResponse();
        response.setContent(content);
        response.setPage(productPage.getNumber());
        response.setSize(productPage.getSize());
        response.setTotalElements(productPage.getTotalElements());
        response.setTotalPages(productPage.getTotalPages());

        return response;
    }
}
