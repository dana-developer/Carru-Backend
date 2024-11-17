package capstone.carru.dto.driver;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReserveRouteMatchingRequest {
    private Long maxWeight;
    private Long minWeight;

    private String departureLocation;
    private BigDecimal departureLatitude;
    private BigDecimal departureLongitude;

    private String destinationLocation;
    private BigDecimal destinationLatitude;
    private BigDecimal destinationLongitude;

    private LocalDateTime estimatedDepartureTime;
    private Long likePrice;
    private Long likeShortOperationDistance;

    private List<Long> stopOverIds;
}
