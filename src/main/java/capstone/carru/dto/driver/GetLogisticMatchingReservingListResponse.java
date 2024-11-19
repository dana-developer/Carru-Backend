package capstone.carru.dto.driver;

import capstone.carru.entity.Product;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetLogisticMatchingReservingListResponse {
    private Long listId;
    private String departureLocation; //출발지 : 차고지
    private BigDecimal departureLatitude;
    private BigDecimal departureLongitude;

    private String destinationLocation; //도착지 : 물류의 목적지
    private BigDecimal destinationLatitude;
    private BigDecimal destinationLongitude;

    private Long weight;
    private Long price;
    private Long operationDistance;
    private Long operationTime;

    public static GetLogisticMatchingReservingListResponse of(Product product) {
        return GetLogisticMatchingReservingListResponse.builder()
                .listId(product.getId())
                .departureLocation(product.getWarehouse().getLocation())
                .departureLatitude(product.getWarehouse().getLocationLat())
                .departureLongitude(product.getWarehouse().getLocationLng())
                .destinationLocation(product.getDestination())
                .destinationLatitude(product.getDestinationLat())
                .destinationLongitude(product.getDestinationLng())
                .weight(product.getWeight())
                .price(product.getPrice())
                .operationDistance(product.getOperationDistance())
                .operationTime(product.getOperationDistance()/50)
                .build();
    }
}
