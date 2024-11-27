package capstone.carru.dto.shipper;

import capstone.carru.entity.Product;
import capstone.carru.entity.User;
import capstone.carru.entity.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FinishedLogisticsResponse {
    private String productName;
    private String warehouseName;
    private String destination;
    private Long weight;
    private Long price;
    private Long operationDistance;
    private Long operationTime;
    private String transporterName;
    private String transporterPhoneNumber;

    public static FinishedLogisticsResponse of(Product product, User transporter) {
        Warehouse warehouse = product.getWarehouse();
        return FinishedLogisticsResponse.builder()
                .productName(product.getName())
                .warehouseName(warehouse.getName())
                .destination(product.getDestination())
                .weight(product.getWeight())
                .price(product.getPrice())
                .operationDistance(product.getOperationDistance())
                .operationTime(product.getOperationDistance() / 50)
                .transporterName(transporter.getName())
                .transporterPhoneNumber(transporter.getPhoneNumber())
                .build();
    }
}