package capstone.carru.service;

import capstone.carru.dto.ErrorCode;
import capstone.carru.dto.manager.GetApprovedDriverLogisticsMatchingListDetailResponse;
import capstone.carru.dto.manager.GetApprovedDriverLogisticsMatchingListResponse;
import capstone.carru.dto.manager.GetApprovedDriverRouteMatchingListDetailResponse;
import capstone.carru.dto.manager.GetApprovedDriverRouteMatchingListResponse;
import capstone.carru.dto.manager.GetApprovedLogisticsListDetailResponse;
import capstone.carru.dto.manager.GetApprovedLogisticsListResponse;
import capstone.carru.dto.manager.GetApprovedUserListResponse;
import capstone.carru.dto.manager.GetApprovingLogisticsListResponse;
import capstone.carru.dto.manager.GetApprovingUserListResponse;
import capstone.carru.entity.Product;
import capstone.carru.entity.ProductReservation;
import capstone.carru.entity.ProductRouteReservation;
import capstone.carru.entity.User;
import capstone.carru.entity.status.ProductStatus;
import capstone.carru.entity.status.UserStatus;
import capstone.carru.exception.InvalidException;
import capstone.carru.repository.product.ProductRepository;
import capstone.carru.repository.productReservation.ProductReservationRepository;
import capstone.carru.repository.productRouteReservation.ProductRouteReservationRepository;
import capstone.carru.repository.stopOver.StopOverRepository;
import capstone.carru.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final ProductReservationRepository productReservationRepository;
    private final StopOverRepository stopOverRepository;
    private final ProductRouteReservationRepository productRouteReservationRepository;

    @Transactional(readOnly = true)
    public Slice<GetApprovingUserListResponse> getApprovingList(String email, int listType, Pageable pageable) {
        userService.validateUser(email);

        Slice<User> users;
        if(listType == 0) { // 화물기사 승인 목록 조회
             users = userRepository.getApprovingList(pageable, UserStatus.DRIVER);
        } else if(listType == 1) {  // 화주 승인 목록 조회
            users = userRepository.getApprovingList(pageable, UserStatus.OWNER);
        } else {
            throw new InvalidException(ErrorCode.INVALID);
        }

        return users.map(GetApprovingUserListResponse::of);
    }

    @Transactional
    public void approveUser(String email, Long userId) {
        userService.validateUser(email);

        User user = userRepository.findByIdAndDeletedDateIsNullAndApprovedDateIsNull(userId)
                .orElseThrow(() -> new InvalidException(ErrorCode.INVALID));

        user.updateApprovedDate();
    }

    @Transactional(readOnly = true)
    public Slice<GetApprovingLogisticsListResponse> getApprovingLogisticsList(String email, Pageable pageable) {
        userService.validateUser(email);

        Slice<Product> products = productRepository.getApprovingList(pageable);

        return products.map(GetApprovingLogisticsListResponse::of);
    }

    @Transactional
    public void approveLogistics(String email, Long productId) {
        userService.validateUser(email);

        Product product = productRepository.findByIdAndDeletedDateIsNullAndApprovedDateIsNull(productId)
                .orElseThrow(() -> new InvalidException(ErrorCode.INVALID));

        product.updateApprovedDate();
        product.updateProductStatus(ProductStatus.APPROVED);
    }

    @Transactional(readOnly = true)
    public Slice<GetApprovedUserListResponse> getApprovedUserList(String email, int listType, Pageable pageable) {
        userService.validateUser(email);

        Slice<User> users;

        if(listType == 0) { // 화물 기사 승인 목록 조회
             users = userRepository.getApprovedList(pageable, UserStatus.DRIVER);
        } else if(listType == 1) {  // 화주 승인 목록 조회
            users = userRepository.getApprovedList(pageable, UserStatus.OWNER);
        } else {
            throw new InvalidException(ErrorCode.INVALID);
        }

        return users.map(GetApprovedUserListResponse::of);
    }

    @Transactional(readOnly = true)
    public Slice<GetApprovedLogisticsListResponse> getApprovedLogisticsList(String email, Pageable pageable) {
        userService.validateUser(email);

        Slice<Product> products = productRepository.getApprovedList(pageable);

        return products.map(GetApprovedLogisticsListResponse::of);
    }

    @Transactional(readOnly = true)
    public GetApprovedLogisticsListDetailResponse getApprovedLogisticsListDetail(String email, Long productId) {
        userService.validateUser(email);

        Product product = productRepository.findByIdAndDeletedDateIsNullAndApprovedDateIsNotNull(productId)
                .orElseThrow(() -> new InvalidException(ErrorCode.INVALID));

        User driver =  productReservationRepository.findByProductIdAndDeletedDateIsNull(productId)
                .map(ProductReservation::getUser)
                .orElse(null);

        if(driver == null) {
            driver = stopOverRepository.findFirstByProductId(productId)
                    .map(stopOver -> stopOver.getProductRouteReservation().getUser())
                    .orElse(null);
        }

        return GetApprovedLogisticsListDetailResponse.of(product, driver);
    }

    @Transactional(readOnly = true)
    public Slice<GetApprovedLogisticsListResponse> getApprovedOwnerLogisticsList(String email, Pageable pageable, Long userId) {
        userService.validateUser(email);

        Slice<Product> products = productRepository.getApprovedListByUser(userId, pageable);

        return products.map(GetApprovedLogisticsListResponse::of);
    }

    @Transactional(readOnly = true)
    public Slice<GetApprovedDriverLogisticsMatchingListResponse> getApprovedDriverLogisticsMatchingList(String email, Pageable pageable, Long userId) {
        userService.validateUser(email);

        Slice<Product> products = productReservationRepository.getApprovedListByDriver(userId, pageable);

        return products.map(GetApprovedDriverLogisticsMatchingListResponse::of);
    }

    @Transactional(readOnly = true)
    public Slice<GetApprovedDriverRouteMatchingListResponse> getApprovedDriverRouteMatchingList(String email, Pageable pageable, Long userId) {
        userService.validateUser(email);

        Slice<ProductRouteReservation> productRouteReservations = productRouteReservationRepository.getApprovedListByDriver(userId, pageable);

        return productRouteReservations.map(GetApprovedDriverRouteMatchingListResponse::of);
    }

    @Transactional(readOnly = true)
    public GetApprovedDriverLogisticsMatchingListDetailResponse getApprovedDriverLogisticsMatchingListDetail(String email, Long productId) {
        userService.validateUser(email);

        Product product = productRepository.findByIdAndDeletedDateIsNullAndApprovedDateIsNotNull(productId)
                .orElseThrow(() -> new InvalidException(ErrorCode.NOT_EXISTS_PRODUCT));

        return GetApprovedDriverLogisticsMatchingListDetailResponse.of(product);
    }

    @Transactional(readOnly = true)
    public GetApprovedDriverRouteMatchingListDetailResponse getApprovedDriverRouteMatchingListDetail(String email, Long listId) {
        userService.validateUser(email);

        ProductRouteReservation productRouteReservation = productRouteReservationRepository
                .findByIdAndDeletedDateIsNull(listId)
                .orElseThrow(() -> new InvalidException(ErrorCode.NOT_EXISTS_PRODUCT));

        return GetApprovedDriverRouteMatchingListDetailResponse.of(productRouteReservation);
    }
}
