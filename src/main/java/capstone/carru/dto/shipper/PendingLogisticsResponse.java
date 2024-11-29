package capstone.carru.dto.shipper;

import capstone.carru.entity.Product;
import capstone.carru.entity.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PendingLogisticsResponse {
    private String productName;
    private Long warehouseId;
    private String warehouseName;
    private BigDecimal warehouseLat;
    private BigDecimal warehouseLng;
    private String destination;
    private BigDecimal destinationLat;
    private BigDecimal destinationLng;
    private Long weight;
    private Long price;
    private Long operationDistance;
    private Long operationTime;
    private LocalDateTime deadline;

    public static PendingLogisticsResponse of(Product product) {
        Warehouse warehouse = product.getWarehouse();
        return PendingLogisticsResponse.builder()
                .productName(product.getName())
                .warehouseId(warehouse.getId())
                .warehouseName(warehouse.getName())
                .warehouseLat(warehouse.getLocationLat())
                .warehouseLng(warehouse.getLocationLng())
                .destination(product.getDestination())
                .destinationLat(product.getDestinationLat())
                .destinationLng(product.getDestinationLng())
                .weight(product.getWeight())
                .price(product.getPrice())
                .operationDistance(product.getOperationDistance())
                .operationTime(product.getOperationDistance() / 50)
                .deadline(product.getDeadline())
                .build();
    }
}