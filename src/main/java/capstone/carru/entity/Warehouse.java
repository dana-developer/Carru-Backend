package capstone.carru.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "warehouses")
@NoArgsConstructor
@Getter
public class Warehouse extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; //창고주

    @Column(length = 100, nullable = false)
    private String name; //창고명

    @Column(length = 2083, nullable = false)
    private String location; //창고 위치

    @Column(precision = 9, scale = 6)
    private BigDecimal locationLat; //창고 위도

    @Column(precision = 9, scale = 6)
    private BigDecimal locationLng; //창고 경도

    @Builder
    public Warehouse(User user, String name, String location,
            BigDecimal locationLat, BigDecimal locationLng) {
        this.user = user;
        this.name = name;
        this.location = location;
        this.locationLat = locationLat;
        this.locationLng = locationLng;
    }
}
