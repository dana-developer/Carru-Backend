package capstone.carru.entity;


import capstone.carru.entity.status.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 320, nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String phoneNumber;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 2083)
    private String location; //차고지 또는 물류 창고 한글 주소

    @Column(precision = 9, scale = 6)
    private BigDecimal locationLat; //차고지 또는 물류 창고 위도

    @Column(precision = 9, scale = 6)
    private BigDecimal locationLng; //차고지 또는 물류 창고 경도

    private LocalDateTime approvedDate; //승인 날짜

    private UserStatus userStatus; //유저 상태(운송자, 관리자, 화주)

    @OneToMany(mappedBy = "user")
    private List<Warehouse> warehouseList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ProductReservation> productReservationList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ProductRouteReservation> productRouteReservationList = new ArrayList<>();

    @Builder
    public User(String name, String email, String phoneNumber,
            String password, String location, UserStatus userStatus,
            BigDecimal locationLat, BigDecimal locationLng) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.location = location;
        this.userStatus = userStatus;
        this.locationLat = locationLat;
        this.locationLng = locationLng;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateApprovedDate() {
        this.approvedDate = LocalDateTime.now();
    }

    public void updateLocation(String location, BigDecimal locationLat, BigDecimal locationLng) {
        this.location = location;
        this.locationLat = locationLat;
        this.locationLng = locationLng;
    }
}
