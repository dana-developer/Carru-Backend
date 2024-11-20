package capstone.carru.dto.driver;

import capstone.carru.entity.Product;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetStopOverDetailResponse {
    private Long stopOverId;
    private String stopOverLocation;
    private BigDecimal stopOverLocationLatitude;
    private BigDecimal stopOverLocationLongitude;

    private String stopOverDestination;
    private BigDecimal stopOverDestinationLatitude;
    private BigDecimal stopOverDestinationLongitude;

    private String productName; //물품명
    private Long weight; //무게
    private Long price; //운송비

    public static GetStopOverDetailResponse of(Product product) {
        return GetStopOverDetailResponse.builder()
                .stopOverId(product.getId())
                .stopOverLocation(product.getWarehouse().getLocation())
                .stopOverLocationLatitude(product.getWarehouse().getLocationLat())
                .stopOverLocationLongitude(product.getWarehouse().getLocationLng())
                .stopOverDestination(product.getDestination())
                .stopOverDestinationLatitude(product.getDestinationLat())
                .stopOverDestinationLongitude(product.getDestinationLng())
                .productName(product.getName())
                .weight(product.getWeight())
                .price(product.getPrice())
                .build();
    }
}
