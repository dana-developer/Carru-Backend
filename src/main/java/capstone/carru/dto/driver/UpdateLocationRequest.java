package capstone.carru.dto.driver;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateLocationRequest {
    private String location;
    private BigDecimal locationLat;
    private BigDecimal locationLng;
}
