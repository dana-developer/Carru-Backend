package capstone.carru.dto.shipper;

import capstone.carru.entity.Product;
import capstone.carru.entity.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class TodoLogisticsResponse {
    private String productName;
    private String warehouseName;
    private String destination;
    private Long weight;
    private Long price;
    private Long operationDistance;
    private Long operationTime;
    private LocalDateTime deadline;

    public static TodoLogisticsResponse of(Product product) {
        Warehouse warehouse = product.getWarehouse();
        return TodoLogisticsResponse.builder()
                .productName(product.getName())
                .warehouseName(warehouse.getName())
                .destination(product.getDestination())
                .weight(product.getWeight())
                .price(product.getPrice())
                .operationDistance(product.getOperationDistance())
                .operationTime(product.getOperationDistance() / 50)
                .build();
    }
}