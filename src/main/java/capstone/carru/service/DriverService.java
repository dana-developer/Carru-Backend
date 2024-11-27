package capstone.carru.service;

import capstone.carru.dto.ErrorCode;
import capstone.carru.dto.driver.GetLogisticMatchingReservingListResponse;
import capstone.carru.dto.driver.GetLogisticsMatchingDetailResponse;
import capstone.carru.dto.driver.GetLogisticsMatchingReservingListDetailResponse;
import capstone.carru.dto.driver.GetRouteMatchingReservingListDetailResponse;
import capstone.carru.dto.driver.GetRouteMatchingResevingListResponse;
import capstone.carru.dto.driver.ReserveRouteMatchingRequest;
import capstone.carru.dto.driver.UpdateLocationRequest;
import capstone.carru.entity.Product;
import capstone.carru.entity.ProductReservation;
import capstone.carru.entity.ProductRouteReservation;
import capstone.carru.entity.StopOver;
import capstone.carru.entity.User;
import capstone.carru.entity.status.ProductStatus;
import capstone.carru.exception.NotFoundException;
import capstone.carru.repository.product.ProductRepository;
import capstone.carru.repository.productReservation.ProductReservationRepository;
import capstone.carru.repository.productRouteReservation.ProductRouteReservationRepository;
import capstone.carru.repository.stopOver.StopOverRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import capstone.carru.dto.driver.GetLogisticsMatchingListRequest;
import capstone.carru.dto.driver.GetLogisticsMatchingListResponse;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final UserService userService;
    private final ProductRepository productRepository;
    private final ProductReservationRepository productReservationRepository;
    private final ProductRouteReservationRepository productRouteReservationRepository;
    private final StopOverRepository stopOverRepository;

    @Transactional(readOnly = true)
    public Slice<GetLogisticsMatchingListResponse> getLogisticsMatchingListRequest(String email, Pageable pageable,
            GetLogisticsMatchingListRequest getLogisticsMatchingListRequest) {

        userService.validateUser(email);

        Slice<Product> products = productRepository.getLogisticsMatchingListRequest(pageable,
                getLogisticsMatchingListRequest.getMaxWeight(), getLogisticsMatchingListRequest.getMinWeight(),
                getLogisticsMatchingListRequest.getSortPrice(), getLogisticsMatchingListRequest.getWarehouseKeyword(),
                getLogisticsMatchingListRequest.getSortOperationDistance());
        return products.map(GetLogisticsMatchingListResponse::of);
    }

    @Transactional(readOnly = true)
    public GetLogisticsMatchingDetailResponse getLogisticsMatching(String email, Long logisticsMatchingId) {
        userService.validateUser(email);

        Product product = productRepository.findByIdAndDeletedDateIsNullAndApprovedDateIsNotNull(logisticsMatchingId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

        if(!product.getProductStatus().equals(ProductStatus.APPROVED)) {
            throw new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT);
        }

        return GetLogisticsMatchingDetailResponse.of(product);
    }

    @Transactional
    public void reserveLogisticsMatching(String email, Long logisticsMatchingId) {
        User user = userService.validateUser(email);

        Product product = productRepository.findByIdAndDeletedDateIsNullAndApprovedDateIsNotNull(logisticsMatchingId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

        if(!product.getProductStatus().equals(ProductStatus.APPROVED)) {
            throw new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT);
        }

        product.updateProductStatus(ProductStatus.DRIVER_TODO);

        ProductReservation productReservation = ProductReservation.builder()
                .user(user)
                .product(product)
                .productStatus(ProductStatus.DRIVER_TODO)
                .build();

        productReservationRepository.save(productReservation);
    }

    @Transactional
    public void reserveRouteMatching(String email, ReserveRouteMatchingRequest reserveRouteMatchingRequest) {
        User user = userService.validateUser(email);

        // 1. 해당 경유지가 존재하는지 확인 + 해당 경유지가 예약되지 않았는지 확인
        List<Product> productList = new ArrayList<>();
        reserveRouteMatchingRequest.getStopOverIds().forEach(stopOverId -> {

            Product product = productRepository.findByIdAndDeletedDateIsNullAndApprovedDateIsNotNull(stopOverId)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

            if(!product.getProductStatus().equals(ProductStatus.APPROVED)) {
                throw new NotFoundException(ErrorCode.INVALID_PRODUCT_STATUS);
            }

            productList.add(product);
        });

        if(productList.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT);
        }

        // 2. 경로 예약 정보 저장
        ProductRouteReservation productRouteReservation = ProductRouteReservation.builder()
                .user(user)
                .maxWeight(reserveRouteMatchingRequest.getMaxWeight())
                .minWeight(reserveRouteMatchingRequest.getMinWeight())
                .departure(reserveRouteMatchingRequest.getDepartureLocation())
                .departureLat(reserveRouteMatchingRequest.getDepartureLatitude())
                .departureLng(reserveRouteMatchingRequest.getDepartureLongitude())
                .destination(reserveRouteMatchingRequest.getDestinationLocation())
                .destinationLat(reserveRouteMatchingRequest.getDestinationLatitude())
                .destinationLng(reserveRouteMatchingRequest.getDestinationLongitude())
                .estimatedDepartureTime(reserveRouteMatchingRequest.getEstimatedDepartureTime())
                .likeMoneyRate(reserveRouteMatchingRequest.getLikePrice())
                .likeShortDistanceRate(reserveRouteMatchingRequest.getLikeShortOperationDistance())
                .productStatus(ProductStatus.DRIVER_TODO)
                .totalWeight(productList.stream().mapToLong(Product::getWeight).sum())
                .totalPrice(productList.stream().mapToLong(Product::getPrice).sum())
                .totalOperationDistance(productList.stream().mapToLong(Product::getOperationDistance).sum())
                .totalOperationTime(productList.stream().mapToLong(Product::getOperationDistance).sum()/50)
                .stopOverCount(productList.size())
                .build();

        productRouteReservationRepository.save(productRouteReservation);

        // 3. 경유지 예약 정보 저장
        productList.forEach(product -> {
            product.updateProductStatus(ProductStatus.DRIVER_TODO);

            StopOver stopOver = StopOver.builder()
                    .productRouteReservation(productRouteReservation)
                    .product(product)
                    .build();

            stopOverRepository.save(stopOver);
        });
    }

    @Transactional(readOnly = true)
    public Slice<GetLogisticMatchingReservingListResponse> getLogisticMatchingReservingList(String email, Pageable pageable, int listType) {
        userService.validateUser(email);

        ProductStatus[] productStatuses = {
                ProductStatus.DRIVER_TODO,       // listType 0
                ProductStatus.DRIVER_INPROGRESS, // listType 1
                ProductStatus.DRIVER_FINISHED    // listType 2
        };

        if (listType < 0 || listType >= productStatuses.length) {
            throw new NotFoundException(ErrorCode.INVALID_PRODUCT_STATUS);
        }

        String productStatus = productStatuses[listType].name();

        Slice<Product> products = productReservationRepository.getReservingList(pageable, email, productStatus);

        return products.map(GetLogisticMatchingReservingListResponse::of);
    }

    @Transactional(readOnly = true)
    public Slice<GetRouteMatchingResevingListResponse> getRouteMatchingReservingList(String email, Pageable pageable, int listType) {
        userService.validateUser(email);

        ProductStatus[] productStatuses = {
                ProductStatus.DRIVER_TODO,       // listType 0
                ProductStatus.DRIVER_INPROGRESS, // listType 1
                ProductStatus.DRIVER_FINISHED    // listType 2
        };

        if (listType < 0 || listType >= productStatuses.length) {
            throw new NotFoundException(ErrorCode.INVALID_PRODUCT_STATUS);
        }

        String productStatus = productStatuses[listType].name();

        Slice<ProductRouteReservation> productRouteReservations
                = productRouteReservationRepository.getReservingList(pageable, email, productStatus);

        return productRouteReservations.map(GetRouteMatchingResevingListResponse::of);
    }

    @Transactional
    public void updateRouteMatchingStatus(String email, Long routeMatchingId, int status) {

        User user = userService.validateUser(email);

        ProductStatus[][] productStatuses = {
                {ProductStatus.DRIVER_TODO,  ProductStatus.DRIVER_INPROGRESS,},     // status 0 (todo를 -> inprogress로)
                {ProductStatus.DRIVER_INPROGRESS, ProductStatus.DRIVER_TODO}, // status 1 (inprogress를 -> todo로)
                {ProductStatus.DRIVER_INPROGRESS, ProductStatus.DRIVER_FINISHED}    // status 2    (inprogress를 finished로)
        };

        if (status < 0 || status >= productStatuses.length) {
            throw new NotFoundException(ErrorCode.INVALID_PRODUCT_STATUS);
        }

        ProductRouteReservation productRouteReservation = productRouteReservationRepository
                .findByIdAndDeletedDateIsNullAndUser(routeMatchingId, user)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

        if(!productRouteReservation.getProductStatus().equals(productStatuses[status][0])) {
            throw new NotFoundException(ErrorCode.INVALID_PRODUCT_STATUS);
        }

        productRouteReservation.updateProductStatus(productStatuses[status][1]);

        List<StopOver> stopOvers = stopOverRepository.findByProductRouteReservation(productRouteReservation);
        stopOvers.forEach(stopOver -> {
            stopOver.getProduct().updateProductStatus(productStatuses[status][1]);
        });
    }

    @Transactional
    public void updateLogisticsMatchingStatus(String email, Long logisticsMatchingId, int status) {

        User user = userService.validateUser(email);

        ProductStatus[][] productStatuses = {
                {ProductStatus.DRIVER_TODO,  ProductStatus.DRIVER_INPROGRESS,},     // status 0 (todo를 -> inprogress로)
                {ProductStatus.DRIVER_INPROGRESS, ProductStatus.DRIVER_TODO}, // status 1 (inprogress를 -> todo로)
                {ProductStatus.DRIVER_INPROGRESS, ProductStatus.DRIVER_FINISHED}    // status 2    (inprogress를 finished로)
        };

        if (status < 0 || status >= productStatuses.length) {
            throw new NotFoundException(ErrorCode.INVALID_PRODUCT_STATUS);
        }


        //이때 logisticsMatchingId는 productId
        ProductReservation productReservation = productReservationRepository
                .findByProductIdAndDeletedDateIsNullAndUser(logisticsMatchingId, user)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

        if(!productReservation.getProductStatus().equals(productStatuses[status][0])) {
            throw new NotFoundException(ErrorCode.INVALID_PRODUCT_STATUS);
        }

        productReservation.updateProductStatus(productStatuses[status][1]);
        productReservation.getProduct().updateProductStatus(productStatuses[status][1]);
    }

    @Transactional(readOnly = true)
    public GetRouteMatchingReservingListDetailResponse getRouteMatchingReservingListDetail(String email, Long routeMatchingId) {
        User user = userService.validateUser(email);

        ProductRouteReservation productRouteReservation = productRouteReservationRepository.findByIdAndDeletedDateIsNullAndUser(routeMatchingId, user)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

        return GetRouteMatchingReservingListDetailResponse.of(productRouteReservation);
    }

    @Transactional(readOnly = true)
    public GetLogisticsMatchingReservingListDetailResponse getLogisticsMatchingReservingListDetail(String email, Long routeMatchingId) {
        User user = userService.validateUser(email);

        ProductReservation productReservation = productReservationRepository.findByProductIdAndDeletedDateIsNullAndUser(routeMatchingId, user)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT));

        return GetLogisticsMatchingReservingListDetailResponse.of(productReservation.getProduct());
    }

    @Transactional
    public void updateLocation(String email, UpdateLocationRequest updateLocationRequest) {
        User user = userService.validateUser(email);

        user.updateLocation(updateLocationRequest.getLocation(),
                updateLocationRequest.getLocationLat(),
                updateLocationRequest.getLocationLng());
    }
}