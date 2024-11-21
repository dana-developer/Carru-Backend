package capstone.carru.service;

import capstone.carru.dto.ErrorCode;
import capstone.carru.dto.User.GetApprovingLogisticsListResponse;
import capstone.carru.dto.User.GetApprovingUserListResponse;
import capstone.carru.entity.Product;
import capstone.carru.entity.User;
import capstone.carru.entity.status.ProductStatus;
import capstone.carru.entity.status.UserStatus;
import capstone.carru.exception.InvalidException;
import capstone.carru.repository.product.ProductRepository;
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
}