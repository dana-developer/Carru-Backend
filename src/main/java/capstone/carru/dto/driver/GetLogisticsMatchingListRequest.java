package capstone.carru.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetLogisticsMatchingListRequest {

    private Long maxWeight;
    private Long minWeight;
    private int sortPrice; //가격 순으로 정렬 (0:오름차순, 1:내림차순)
    private int sortOperationDistance; //운행 거리 순으로 정렬 (0:오름차순, 1:내림차순)
    private String warehouseKeyword; //창고 위치로 검색
}
