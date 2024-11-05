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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_route_reservations")
@NoArgsConstructor
public class ProductRouteReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_route_reservation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; //예약한 운송자

    @OneToMany(mappedBy = "productRouteReservation")
    private List<StopOver> stopOverList = new ArrayList<>();

    @Column(nullable = false)
    private Long likeMoneyRate; //돈을 좋아하는 수치

    @Column(nullable = false)
    private Long likeShortDistanceRate; //짧은 거리를 좋아하는 수치

    @Column(nullable = false)
    private Long maxWeight;

    @Column(nullable = false)
    private Long minWeight;

    @Column(nullable = false)
    private ProductStatus productStatus; //예약 상태

    @Column(length = 2083, nullable = false)
    private String destination; //목적지
    @Column(precision = 9, scale = 6)
    private BigDecimal destinationLat; //목적지 위도
    @Column(precision = 9, scale = 6)
    private BigDecimal destinationLng; //목적지 경도

    @Column(length = 2083, nullable = false)
    private String departure; //출발지
    @Column(precision = 9, scale = 6)
    private BigDecimal departureLat; //출발지 위도
    @Column(precision = 9, scale = 6)
    private BigDecimal departureLng; //출발지 경도

    @Column(nullable = false)
    private LocalDateTime estimatedDepartureTime; //출발 예정 시간
}
