package capstone.carru.repository.product;

import capstone.carru.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRepositoryCustom {
    Slice<Product> getLogisticsMatchingListRequest(
            Pageable pageable, Long maxWeight, Long minWeight,
            int sortPrice, String warehouseKeyword, int sortOperationDistance);
}
