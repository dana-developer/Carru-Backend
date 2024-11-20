package capstone.carru.dto.shipper;

import capstone.carru.entity.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SearchWarehouseRequest {
    private Long id;
    private String name;
    private String location;
    private BigDecimal locationLat;
    private BigDecimal locationLng;

    public static SearchWarehouseRequest fromEntity(Warehouse warehouse) {
        return new SearchWarehouseRequest(
                warehouse.getId(),
                warehouse.getName(),
                warehouse.getLocation(),
                warehouse.getLocationLat(),
                warehouse.getLocationLng()
        );
    }
}