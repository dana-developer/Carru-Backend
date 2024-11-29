package capstone.carru.dto.shipper;

import capstone.carru.entity.Product;
import capstone.carru.entity.User;
import capstone.carru.entity.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class ApprovedLogisticsResponse {
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
    private String transporterName;
    private String transporterPhoneNumber;

    public static ApprovedLogisticsResponse of(Product product, User transporter) {
        Warehouse warehouse = product.getWarehouse();
        return ApprovedLogisticsResponse.builder()
                .productName(product.getName())
                .warehouseId(warehouse.getId())
                .warehouseName(warehouse.getName())
                .warehouseLat(warehouse.getLocationLat())
                .warehouseLng(warehouse.getLocationLng())
                .destination(product.getDestination())
                .destination(product.getDestination())
                .destinationLat(product.getDestinationLat())
                .destinationLng(product.getDestinationLng())
                .weight(product.getWeight())
                .price(product.getPrice())
                .operationDistance(product.getOperationDistance())
                .operationTime(product.getOperationDistance() / 50)
                .transporterName(transporter.getName())
                .transporterPhoneNumber(transporter.getPhoneNumber())
                .build();
    }
}