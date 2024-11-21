package capstone.carru.dto.shipper;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PendingLogisticsResponse {
    private String location;
    private String destination;
    private double weight;
    private double cost;
    private Long operationDistance;
    private Long operationTime;
    private LocalDateTime deadline;
}
