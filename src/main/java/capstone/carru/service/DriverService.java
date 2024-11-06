package capstone.carru.service;

import capstone.carru.dto.ErrorCode;
import capstone.carru.dto.driver.GetLogisticsMatchingDetailResponse;
import capstone.carru.entity.Product;
import capstone.carru.entity.User;
import capstone.carru.entity.status.ProductStatus;
import capstone.carru.exception.NotFoundException;
import capstone.carru.repository.product.ProductRepository;
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

        if(!product.getProductStatus().equals(ProductStatus.TODO)) {
            throw new NotFoundException(ErrorCode.NOT_EXISTS_PRODUCT);
        }

        return GetLogisticsMatchingDetailResponse.of(product);
    }
}