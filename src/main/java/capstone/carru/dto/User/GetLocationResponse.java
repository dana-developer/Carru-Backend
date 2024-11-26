package capstone.carru.dto.User;

import capstone.carru.entity.User;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetLocationResponse {
    private String location;
    private BigDecimal locationLat;
    private BigDecimal locationLng;

    public static GetLocationResponse of(User user) {
        return GetLocationResponse.builder()
                .location(user.getLocation())
                .locationLat(user.getLocationLat())
                .locationLng(user.getLocationLng())
                .build();
    }
}
