package capstone.carru.dto.driver;

import capstone.carru.entity.Product;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetLogisticsMatchingListResponse {
    private Long productId; //상품 id
    private String departureLocation; //출발지
    private String destinationLocation; //목적지
    private Long price; //운송비
    private Long weight; //무게
    private Long operationDistance; //운행 거리
    private Long operationTime; //운행 시간
    private LocalDateTime deadLine; //마감 시간(운송 기한)

    public static GetLogisticsMatchingListResponse of(Product product) {
        return GetLogisticsMatchingListResponse.builder()
                .productId(product.getId())
                .departureLocation(product.getWarehouse().getLocation())
                .destinationLocation(product.getDestination())
                .price(product.getPrice())
                .weight(product.getWeight())
                .operationDistance(product.getOperationDistance())
                .operationTime(product.getOperationDistance()/50)
                .deadLine(product.getDeadline())
                .build();
    }
}
