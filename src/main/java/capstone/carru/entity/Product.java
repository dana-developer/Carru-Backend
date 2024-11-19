package capstone.carru.entity;

import capstone.carru.entity.status.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Getter
public class Product extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse; //창고주

    @OneToMany(mappedBy = "product")
    private List<ProductReservation> productReservationList = new ArrayList<>();

    @Column(length = 100, nullable = false)
    private String name; //상품명

    @Column(length = 2083, nullable = false)
    private String destination; //배송지

    @Column(precision = 9, scale = 6)
    private BigDecimal destinationLat; //배송지 위도

    @Column(precision = 9, scale = 6)
    private BigDecimal destinationLng; //배송지 경도

    @Column(nullable = false)
    private ProductStatus productStatus; //상품 상태

    @Column(nullable = false)
    private Long price; //상품 가격

    @Max(100)
    private Long weight; //상품 무게

    @Column(nullable = false)
    private LocalDateTime deadline; //마감시간

    private LocalDateTime approvedDate; //승인 날짜

    @Column(nullable = false)
    private Long operationDistance; //운행 거리 (km) (물류 등록시 계산)

    public void updateProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
}
