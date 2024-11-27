package capstone.carru.dto.manager;

import capstone.carru.entity.Product;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetApprovedDriverLogisticsMatchingListResponse {
    private Long listId;
    private String destination;
    private String departure;
    private Long weight;
    private Long price;
    private Long operationDistance;
    private Long operationTime;
    private LocalDateTime deadline;

    public static GetApprovedDriverLogisticsMatchingListResponse of(Product product) {
        return GetApprovedDriverLogisticsMatchingListResponse.builder()
                .listId(product.getId())
                .destination(product.getDestination())
                .departure(product.getWarehouse().getUser().getLocation())
                .weight(product.getWeight())
                .price(product.getPrice())
                .operationDistance(product.getOperationDistance())
                .operationTime(product.getOperationDistance()/50)
                .deadline(product.getDeadline())
                .build();
    }
}
