package capstone.carru.dto.manager;

import capstone.carru.entity.Product;
import capstone.carru.entity.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetApprovedLogisticsListDetailResponse {
    private Long productId; //상품 id
    private String productName; //상품명

    private String departureLocation; //출발지
    private BigDecimal departureLatitude;
    private BigDecimal departureLongitude;

    private String departureName; //출발 창고명

    private String destinationLocation; //목적지
    private BigDecimal destinationLatitude;
    private BigDecimal destinationLongitude;

    private Long price; //운송비

    private Long weight; //무게
    private Long operationDistance; //운행 거리
    private Long operationTime; //운행 시간
    private LocalDateTime deadLine; //마감 시간(운송 기한)

    private String ownerName; //화주 이름

    private String driverName; //화물기사 이름
    private String driverEmail; //화물기사 이메일
    private String driverPhoneNumber; //화물기사 전화번호

    private String driveStatus; //운송 상태

    public static GetApprovedLogisticsListDetailResponse of(Product product, User driver) {
        return GetApprovedLogisticsListDetailResponse.builder()
                .productId(product.getId())
                .productName(product.getName())
                .departureLocation(product.getWarehouse().getLocation())
                .departureLatitude(product.getWarehouse().getLocationLat())
                .departureLongitude(product.getWarehouse().getLocationLng())
                .departureName(product.getWarehouse().getName())
                .destinationLocation(product.getDestination())
                .destinationLatitude(product.getDestinationLat())
                .destinationLongitude(product.getDestinationLng())
                .price(product.getPrice())
                .weight(product.getWeight())
                .operationDistance(product.getOperationDistance())
                .operationTime(product.getOperationDistance()/50)
                .deadLine(product.getDeadline())
                .ownerName(product.getWarehouse().getUser().getName())
                .driverName(driver != null ? driver.getName() : "아직 배정받지 못했습니다.")
                .driverEmail(driver != null ? driver.getEmail() : "아직 배정받지 못했습니다.")
                .driverPhoneNumber(driver != null ? driver.getPhoneNumber() : "아직 배정받지 못했습니다.")
                .driveStatus(String.valueOf(product.getProductStatus()))
                .build();
    }
}
