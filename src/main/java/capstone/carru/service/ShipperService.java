package capstone.carru.service;

import capstone.carru.dto.ErrorCode;
import capstone.carru.dto.shipper.PendingLogisticsListResponse;
import capstone.carru.dto.shipper.PendingLogisticsResponse;
import capstone.carru.dto.shipper.RegisterLogisticsRequest;
import capstone.carru.entity.User;
import capstone.carru.entity.Product;
import capstone.carru.entity.Warehouse;
import capstone.carru.entity.status.ProductStatus;
import capstone.carru.exception.NotFoundException;
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
        double earthRadius = 6371;
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
        User user = userService.validateUser(email);

        Warehouse warehouse = warehouseRepository.findById(registerLogisticsRequest.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("해당 창고를 찾을 수 없습니다."));

        // 거리 계산
        double distance = calculateDistance(
                user.getLocationLat().doubleValue(),
                user.getLocationLng().doubleValue(),
                warehouse.getLocationLat().doubleValue(),
                warehouse.getLocationLng().doubleValue()
        );

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

        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<PendingLogisticsListResponse> getPendingLogistics(String email) {
        User user = userService.validateUser(email);

        List<Product> products = productRepository.findAllByWarehouse_UserAndProductStatus(user, ProductStatus.WAITING);

        return products.stream()
                .map(product -> {
                    Warehouse warehouse = product.getWarehouse();
                    return new PendingLogisticsListResponse(
                            product.getId(),
                            warehouse.getName(),
                            product.getDestination(),
                            product.getWeight(),
                            product.getPrice(),
                            product.getOperationDistance(),
                            product.getOperationDistance()/50,
                            product.getDeadline()
                    );
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public PendingLogisticsResponse getPendingLogisticsDetail(String email, Long id) {
        User user = userService.validateUser(email);

        Product product = productRepository.findByIdAndProductStatus(id, ProductStatus.WAITING)
                .orElseThrow(() -> new IllegalArgumentException("해당 미승인 물류를 찾을 수 없습니다."));

        Warehouse warehouse = product.getWarehouse();

        return new PendingLogisticsResponse(
                warehouse.getName(),
                product.getDestination(),
                product.getWeight(),
                product.getPrice(),
                product.getOperationDistance(),
                product.getOperationDistance()/50,
                product.getDeadline()
        );
    }

    @Transactional
    public void deletePendingLogistics(String email, Long id) {
        User user = userService.validateUser(email);
        Product logistics = productRepository.findByIdAndWarehouseUserEmailAndApprovedDateIsNull(id, email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

        productRepository.delete(logistics);
    }
}