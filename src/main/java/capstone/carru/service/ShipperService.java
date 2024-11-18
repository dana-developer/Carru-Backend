package capstone.carru.service;

import capstone.carru.dto.shipper.RegisterLogisticsRequest;
import capstone.carru.entity.Product;
import capstone.carru.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipperService {

    private final ProductRepository productRepository;

    @Transactional
    public void registerLogistics(RegisterLogisticsRequest registerLogisticsRequest) {
        // Product 엔티티를 Builder를 사용하여 생성
        Product product = Product.builder()
                .name(registerLogisticsRequest.getName())
                .destination(registerLogisticsRequest.getDestination())
                .price(Math.round(registerLogisticsRequest.getCost()))
                .weight(Math.round(registerLogisticsRequest.getWeight()))
                .deadline(registerLogisticsRequest.getDeadline())
                .build();

        // 엔티티 저장
        productRepository.save(product);
    }
}