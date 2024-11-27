package capstone.carru.dto.manager;

import capstone.carru.dto.driver.GetStopOverDetailResponse;
import capstone.carru.entity.ProductRouteReservation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetApprovedDriverRouteMatchingListDetailResponse {
    private Long listId; //예약목록 id
    private String status; // 운송 상태

    private String departureLocation; //출발지
    private BigDecimal departureLatitude;
    private BigDecimal departureLongitude;

    private String destinationLocation; //목적지
    private BigDecimal destinationLatitude;
    private BigDecimal destinationLongitude;

    private Long totalWeight;
    private Long totalPrice;
    private Long totalOperationDistance;
    private Long totalOperationTime;

    private LocalDateTime startTime; //운행 시작 시간
    private int stopOverCount; //경유지 개수
    private List<GetStopOverDetailResponse> stopOverList;

    public static GetApprovedDriverRouteMatchingListDetailResponse of(
            ProductRouteReservation productRouteReservation){
        return GetApprovedDriverRouteMatchingListDetailResponse.builder()
                .listId(productRouteReservation.getId())
                .status(productRouteReservation.getProductStatus().name())
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
                .startTime(productRouteReservation.getEstimatedDepartureTime())
                .stopOverCount(productRouteReservation.getStopOverCount())
                .stopOverList(productRouteReservation.getStopOverList().stream()
                        .map(stopOver -> GetStopOverDetailResponse.of(stopOver.getProduct()))
                        .collect(Collectors.toList()))
                .build();
    }
}
