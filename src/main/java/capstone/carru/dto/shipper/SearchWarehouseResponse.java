package capstone.carru.dto.shipper;

import capstone.carru.entity.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SearchWarehouseResponse {
    private Long warehouseId;
    private String name;
    private String location;
    private BigDecimal locationLat;
    private BigDecimal locationLng;

    public static SearchWarehouseResponse fromEntity(Warehouse warehouse) {
        return new SearchWarehouseResponse(
                warehouse.getId(),
                warehouse.getName(),
                warehouse.getLocation(),
                warehouse.getLocationLat(),
                warehouse.getLocationLng()
        );
    }
}