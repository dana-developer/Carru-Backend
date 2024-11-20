package capstone.carru.service;

import capstone.carru.dto.shipper.RegisterLogisticsRequest;
import capstone.carru.entity.User;
import capstone.carru.entity.Product;
import capstone.carru.entity.Warehouse;
import capstone.carru.entity.status.ProductStatus;
import capstone.carru.repository.product.ProductRepository;
import capstone.carru.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipperService {

    private final UserService userService;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    // 거리 계산 메소드
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371; // 지구 반지름 (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    @Transactional
    public void registerLogistics(String email, RegisterLogisticsRequest registerLogisticsRequest) {
        // 창고 키워드로 조회
        List<Warehouse> warehouses = warehouseRepository.findByNameContainingOrLocationContaining(
                registerLogisticsRequest.getDestination(),
                registerLogisticsRequest.getDestination()
        );

        if (warehouses.isEmpty()) {
            throw new IllegalArgumentException("해당 키워드와 일치하는 창고가 없습니다.");
        }

        // 첫 번째 창고를 선택 (필요시 추가 조건으로 특정 창고 선택 가능)
        Warehouse warehouse = warehouses.get(0);

        // 화주 위치 가져오기
        User user = userService.validateUser(email);

        // 거리 계산
        double distance = calculateDistance(
                user.getLocationLat().doubleValue(),
                user.getLocationLng().doubleValue(),
                warehouse.getLocationLat().doubleValue(),
                warehouse.getLocationLng().doubleValue()
        );

        // Product 엔티티를 Builder를 사용하여 생성
        Product product = Product.builder()
                .name(registerLogisticsRequest.getName())
                .destination(registerLogisticsRequest.getDestination())
                .destinationLat(warehouse.getLocationLat())
                .destinationLng(warehouse.getLocationLng())
                .price(Math.round(registerLogisticsRequest.getCost()))
                .weight(Math.round(registerLogisticsRequest.getWeight()))
                .deadline(registerLogisticsRequest.getDeadline())
                .operationDistance(Math.round(distance))
                .productStatus(ProductStatus.WAITING)
                .build();

        // 엔티티 저장
        productRepository.save(product);
    }
}