package capstone.carru.driver.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "drivers")
public class User extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 320, nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String phoneNumber;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 2083, nullable = false)
    private String carLocation; //차고지 한글 주소

    @Column(precision = 9, scale = 6)
    private BigDecimal carLocationLat; //차고지 위도

    @Column(precision = 9, scale = 6)
    private BigDecimal carLocationLng; //차고지 경도

    private LocalDateTime approvedDate; //승인날짜
}
