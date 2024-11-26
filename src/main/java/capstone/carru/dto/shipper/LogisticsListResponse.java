package capstone.carru.dto.shipper;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LogisticsListResponse {
    private Long productId;
    private String location;
    private String destination;
    private Long weight;
    private Long cost;
    private Long operationDistance;
    private Long operationTime;
    private LocalDateTime deadline;
}
