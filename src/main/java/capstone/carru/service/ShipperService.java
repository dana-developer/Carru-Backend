package capstone.carru.service;

import capstone.carru.dto.ErrorCode;
import capstone.carru.dto.shipper.*;
import capstone.carru.entity.ProductReservation;
import capstone.carru.entity.User;
import capstone.carru.entity.Product;
import capstone.carru.entity.Warehouse;
import capstone.carru.entity.status.ProductStatus;
import capstone.carru.exception.InvalidException;
import capstone.carru.exception.NotFoundException;
import capstone.carru.repository.product.ProductRepository;
import capstone.carru.repository.productReservation.ProductReservationRepository;
import capstone.carru.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipperService {

    private final UserService userService;
    private final ProductRepository productRepository;
    private final ProductReservationRepository productReservationRepository;
    private final WarehouseRepository warehouseRepository;
    private final PriceService priceService;

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
        long distance = Math.round(calculateDistance(
                user.getLocationLat().doubleValue(),
                user.getLocationLng().doubleValue(),
                destination.getLocationLat().doubleValue(),
                destination.getLocationLng().doubleValue()
        ));

        Warehouse userWarehouse = warehouseRepository.findFirstByUserAndDeletedDateIsNull(user)
                .orElseThrow(() -> new InvalidException(ErrorCode.NOT_EXISTS_USERWAREHOUSE));

        Long shippingPrice = priceService.calculateShippingPrice(registerLogisticsRequest.getWeight(), distance);

        Product product = Product.builder()
                .name(registerLogisticsRequest.getName())
                .destination(destination.getLocation())
                .destinationLat(destination.getLocationLat())
                .destinationLng(destination.getLocationLng())
                .price(shippingPrice)
                .weight(registerLogisticsRequest.getWeight())
                .deadline(registerLogisticsRequest.getDeadline())
                .operationDistance(distance)
                .productStatus(ProductStatus.WAITING)
                .warehouse(userWarehouse) // 화주의 창고 정보(화주는 창고를 하나만 가질 수 있음)
                .destinationName(destination.getName())
                .build();

        // 엔티티 저장
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<LogisticsListResponse> getPendingLogistics(String email) {
        User user = userService.validateUser(email);

        List<Product> products = productRepository.findAllByWarehouse_UserAndProductStatusAndDeletedDateIsNull(user, ProductStatus.WAITING)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

        return products.stream()
                .map(product -> {
                    Warehouse warehouse = product.getWarehouse();
                    return new LogisticsListResponse(
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

        Product product = productRepository.findByIdAndDeletedDateIsNullAndProductStatus(id, ProductStatus.WAITING)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

        return PendingLogisticsResponse.of(product);
    }

    @Transactional
    public void deletePendingLogistics(String email, Long id) {
        User user = userService.validateUser(email);
        Product logistics = productRepository.findByIdAndWarehouseUserEmailAndDeletedDateIsNullAndApprovedDateIsNull(id, email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

        logistics.updateDeletedDate(LocalDateTime.now());
        productRepository.save(logistics);
    }

    @Transactional
    public void updatePendingLogistics(String email, Long id, RegisterLogisticsRequest updateRequest) {
        User user = userService.validateUser(email);

        Product logistics = productRepository.findByIdAndWarehouseUserEmailAndDeletedDateIsNullAndApprovedDateIsNull(id, email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

        if(updateRequest.getWarehouseId() != null) {
            Warehouse destination = warehouseRepository.findById(updateRequest.getWarehouseId())
                    .orElseThrow(() -> new InvalidException(ErrorCode.NOT_EXISTS_WAREHOUSE));

            Warehouse userWarehouse = warehouseRepository.findFirstByUserAndDeletedDateIsNull(user)
                    .orElseThrow(() -> new InvalidException(ErrorCode.NOT_EXISTS_USERWAREHOUSE));

            // 새로운 거리 계산
            long distance = Math.round(calculateDistance(
                    user.getLocationLat().doubleValue(),
                    user.getLocationLng().doubleValue(),
                    destination.getLocationLat().doubleValue(),
                    destination.getLocationLng().doubleValue()
            ));

            Long shippingPrice = priceService.calculateShippingPrice(updateRequest.getWeight(), distance);

            // 업데이트
            logistics.updateDetails(
                    updateRequest.getName(),
                    destination.getLocation(),
                    destination.getLocationLat(),
                    destination.getLocationLng(),
                    shippingPrice,
                    updateRequest.getWeight(),
                    updateRequest.getDeadline(),
                    distance,
                    userWarehouse
            );
        } else {

            Long shippingPrice = priceService.calculateShippingPrice(updateRequest.getWeight(), logistics.getOperationDistance());

            logistics.updateOtherDetails(
                    updateRequest.getName(),
                    updateRequest.getWeight(),
                    updateRequest.getDeadline(),
                    shippingPrice
            );
        }

        productRepository.save(logistics);
    }

    @Transactional(readOnly = true)
    public List<LogisticsListResponse> getApprovedLogistics(String email, int listType) {
        User user = userService.validateUser(email);

        ProductStatus s;
        if (listType == 0) s = ProductStatus.DRIVER_TODO;
        else if (listType == 1) s = ProductStatus.DRIVER_INPROGRESS;
        else if (listType == 2) s = ProductStatus.DRIVER_FINISHED;
        else if(listType == 3) s = ProductStatus.APPROVED;
        else throw new InvalidException(ErrorCode.INVALID_PRODUCT_STATUS);

        List<Product> products = productRepository.findAllByWarehouse_UserAndProductStatusAndDeletedDateIsNull(user, s)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

        return products.stream()
                .map(product -> {
                    Warehouse warehouse = product.getWarehouse();
                    return new LogisticsListResponse(
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
    public ApprovedLogisticsResponse getApprovedLogisticsDetail(String email, Long id, int listType) {
        userService.validateUser(email);

        ProductStatus s = null;
        if (listType == 0) s = ProductStatus.DRIVER_TODO;
        else if (listType == 1) s = ProductStatus.DRIVER_INPROGRESS;
        else if (listType == 2) s = ProductStatus.DRIVER_FINISHED;
        else if (listType == 3) s = ProductStatus.APPROVED;
        else throw new InvalidException(ErrorCode.INVALID_PRODUCT_STATUS);

        // Product 조회
        Product product = productRepository.findByIdAndDeletedDateIsNullAndProductStatus(id, s)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

        if (listType == 3) {
            Warehouse warehouse = product.getWarehouse();
            return ApprovedLogisticsResponse.builder()
                    .productName(product.getName())
                    .warehouseId(warehouse.getId())
                    .warehouseName(warehouse.getName())
                    .warehouseLat(warehouse.getLocationLat())
                    .warehouseLng(warehouse.getLocationLng())
                    .destination(product.getDestination())
                    .destinationLat(product.getDestinationLat())
                    .destinationLng(product.getDestinationLng())
                    .weight(product.getWeight())
                    .price(product.getPrice())
                    .operationDistance(product.getOperationDistance())
                    .operationTime(product.getOperationDistance() / 50)
                    .transporterName(null)
                    .transporterPhoneNumber(null)
                    .build();
        } else {
            ProductReservation reservation = productReservationRepository.findByProductIdAndDeletedDateIsNull(id)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_RESERVATION));
            User transporter = reservation.getUser();

            return ApprovedLogisticsResponse.of(product, transporter);
        }
    }
}