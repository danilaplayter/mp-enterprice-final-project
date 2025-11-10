/* @MENTEE_POWER (C)2025 */
package ru.mentee.power.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.mentee.power.api.generated.dto.ProductCreateRequest;
import ru.mentee.power.api.generated.dto.ProductResponse;
import ru.mentee.power.api.generated.dto.ProductUpdateRequest;
import ru.mentee.power.domain.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toProduct(ProductCreateRequest productCreateRequest);

    ProductResponse toProductResponse(Product product);

    @Mapping(target = "name", source = "productUpdateRequest.name")
    @Mapping(target = "description", source = "productUpdateRequest.description")
    @Mapping(target = "price", source = "productUpdateRequest.price")
    @Mapping(target = "stockQuantity", source = "productUpdateRequest.stockQuantity")
    Product updateProductFromRequest(Product product, ProductUpdateRequest productUpdateRequest);
}
