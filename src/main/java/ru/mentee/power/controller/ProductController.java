/* @MENTEE_POWER (C)2025 */
package ru.mentee.power.controller;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.mentee.power.api.generated.controller.ProductsApi;
import ru.mentee.power.api.generated.dto.ProductCreateRequest;
import ru.mentee.power.api.generated.dto.ProductPageResponse;
import ru.mentee.power.api.generated.dto.ProductResponse;
import ru.mentee.power.api.generated.dto.ProductUpdateRequest;
import ru.mentee.power.service.ProductService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController implements ProductsApi {
    private final ProductService productService;

    @Override
    public ResponseEntity<ProductResponse> createProduct(
            @Valid ProductCreateRequest productCreateRequest) {
        log.info("REST request to create product: {}", productCreateRequest.getName());
        ProductResponse productResponse = productService.createProduct(productCreateRequest);

        return ResponseEntity.created(URI.create("/api/v1/create")).body(productResponse);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(UUID productId) {
        log.info("REST request to delete product with id: {}", productId);
        productService.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ProductPageResponse> getAllProducts(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean inStock,
            Integer page,
            Integer size,
            String sort) {
        log.info(
                "REST request to get all products - page: {}, size: {}, sort: {}",
                page,
                size,
                sort);

        PageRequest pageable = createPageable(page, size, sort);

        ProductPageResponse response =
                productService.getAllProducts(name, minPrice, maxPrice, inStock, pageable);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(UUID productId) {
        log.info("REST request to get product with id: {}", productId);
        ProductResponse response = productService.getProductById(productId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductResponse> updateProduct(
            UUID productId, @Valid ProductUpdateRequest productUpdateRequest) {
        log.info("REST request to update product with id: {}", productId);
        ProductResponse response = productService.updateProduct(productId, productUpdateRequest);
        return ResponseEntity.ok(response);
    }

    private PageRequest createPageable(Integer page, Integer size, String sort) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        if (sort != null && !sort.isBlank()) {
            String[] sortParams = sort.split(",");
            String field = sortParams[0];
            Sort.Direction direction =
                    sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1])
                            ? Sort.Direction.DESC
                            : Sort.Direction.ASC;

            return PageRequest.of(pageNumber, pageSize, Sort.by(direction, field));
        }

        return PageRequest.of(pageNumber, pageSize);
    }
}
