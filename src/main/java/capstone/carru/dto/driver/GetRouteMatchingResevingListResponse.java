package capstone.carru.dto.driver;

import capstone.carru.entity.ProductRouteReservation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetRouteMatchingResevingListResponse {
    private Long listId;
    private LocalDateTime startTime; //운행 시작 시간
    private String departureLocation; //출발지 : 차고지
    private BigDecimal departureLatitude;
    private BigDecimal departureLongitude;

    private String destinationLocation; //도착지 : 물류의 목적지
    private BigDecimal destinationLatitude;
    private BigDecimal destinationLongitude;

    private Long totalWeight;
    private Long totalPrice;
    private Long totalOperationDistance;
    private Long totalOperationTime;

    public static GetRouteMatchingResevingListResponse of(ProductRouteReservation productRouteReservation) {
        return GetRouteMatchingResevingListResponse.builder()
                .listId(productRouteReservation.getId())
                .startTime(productRouteReservation.getEstimatedDepartureTime())
                .departureLocation(productRouteReservation.getDeparture())
                .departureLatitude(productRouteReservation.getDepartureLat())
                .departureLongitude(productRouteReservation.getDepartureLng())

                .destinationLocation(productRouteReservation.getDestination())
                .destinationLatitude(productRouteReservation.getDestinationLat())
                .destinationLongitude(productRouteReservation.getDestinationLng())

                .totalWeight(productRouteReservation.getTotalWeight())
                .totalPrice(productRouteReservation.getTotalPrice())
                .totalOperationDistance(productRouteReservation.getTotalOperationDistance())
                .totalOperationTime(productRouteReservation.getTotalOperationTime())
                .build();
    }
}
