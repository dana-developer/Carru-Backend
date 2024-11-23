package capstone.carru.dto.manager;

import capstone.carru.entity.Product;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetApprovingLogisticsListResponse {
    private Long logisticsId;
    private String destination;
    private String departure;
    private Long weight;
    private Long price;
    private Long operationDistance;
    private Long operationTime;
    private LocalDateTime deadline;
    private String ownerName; //화주 이름

    public static GetApprovingLogisticsListResponse of(Product product) {
        return GetApprovingLogisticsListResponse.builder()
                .logisticsId(product.getId())
                .destination(product.getDestination())
                .departure(product.getWarehouse().getUser().getLocation())
                .weight(product.getWeight())
                .price(product.getPrice())
                .operationDistance(product.getOperationDistance())
                .operationTime(product.getOperationDistance()/50)
                .deadline(product.getDeadline())
                .ownerName(product.getWarehouse().getUser().getName())
                .build();
    }
}
