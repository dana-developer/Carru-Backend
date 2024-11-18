package capstone.carru.dto.shipper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RegisterLogisticsRequest {

    private String name;           // 물류 이름
    private double weight;         // 물류 무게
    private double cost;           // 비용
    private String destination;    // 도착지
    private LocalDateTime deadline; // 도착기한
}