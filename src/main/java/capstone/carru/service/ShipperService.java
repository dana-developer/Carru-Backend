package capstone.carru.service;

import capstone.carru.dto.ErrorCode;
import capstone.carru.dto.shipper.RegisterLogisticsRequest;
import capstone.carru.entity.User;
import capstone.carru.entity.Product;
import capstone.carru.entity.Warehouse;
import capstone.carru.entity.status.ProductStatus;
import capstone.carru.exception.InvalidException;
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
        // 창고 정보를 받아옴
        Warehouse destination = warehouseRepository.findById(registerLogisticsRequest.getWarehouseId())
                .orElseThrow(() -> new InvalidException(ErrorCode.NOT_EXISTS_WAREHOUSE));

        // 화주 정보 조회
        User user = userService.validateUser(email);

        // 거리 계산
        double distance = calculateDistance(
                user.getLocationLat().doubleValue(),
                user.getLocationLng().doubleValue(),
                destination.getLocationLat().doubleValue(),
                destination.getLocationLng().doubleValue()
        );

        Warehouse userWarehouse = warehouseRepository.findFirstByUserAndDeletedDateIsNull(user)
                .orElseThrow(() -> new InvalidException(ErrorCode.NOT_EXISTS_USERWAREHOUSE));

        // Product 엔티티를 Builder를 사용하여 생성
        Product product = Product.builder()
                .name(registerLogisticsRequest.getName())
                .destination(destination.getLocation())
                .destinationLat(destination.getLocationLat())
                .destinationLng(destination.getLocationLng())
                .price(Math.round(registerLogisticsRequest.getCost()))
                .weight(Math.round(registerLogisticsRequest.getWeight()))
                .deadline(registerLogisticsRequest.getDeadline())
                .operationDistance(Math.round(distance))
                .productStatus(ProductStatus.WAITING)
                .warehouse(userWarehouse) // 화주의 창고 정보(화주는 창고를 하나만 가질 수 있음)
                .build();

        // 엔티티 저장
        productRepository.save(product);
    }
}