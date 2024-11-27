package capstone.carru.dto.shipper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RegisterLogisticsRequest {
    private Long warehouseId;      // 창고 ID
    private String name;           // 물류 이름
    private Long weight;         // 물류 무게
    private LocalDateTime deadline; // 도착기한
}