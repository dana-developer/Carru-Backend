package capstone.carru.service;

import capstone.carru.dto.shipper.PendingLogisticsResponse;
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
        // 창고 정보를 받아옴
        Warehouse warehouse = warehouseRepository.findById(registerLogisticsRequest.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("해당 창고를 찾을 수 없습니다."));

        // 화주 정보 조회
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
                .destination(warehouse.getLocation())
                .destinationLat(warehouse.getLocationLat())
                .destinationLng(warehouse.getLocationLng())
                .price(Math.round(registerLogisticsRequest.getCost()))
                .weight(Math.round(registerLogisticsRequest.getWeight()))
                .deadline(registerLogisticsRequest.getDeadline())
                .operationDistance(Math.round(distance))
                .productStatus(ProductStatus.WAITING)
                .warehouse(warehouse)
                .build();

        // 엔티티 저장
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<PendingLogisticsResponse> getPendingLogistics(String email) {
        User user = userService.validateUser(email);
        List<Product> products = productRepository.findAllByWarehouse_UserAndProductStatus(user, ProductStatus.WAITING);

        return products.stream()
                .map(product -> new PendingLogisticsResponse(
                        user.getLocation(),
                        product.getDestination(),
                        product.getWeight(),
                        product.getPrice(),
                        product.getOperationDistance(),
                        product.getOperationDistance()/50,
                        product.getDeadline()))
                .toList();
    }
}