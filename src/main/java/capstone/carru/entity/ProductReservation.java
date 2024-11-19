package capstone.carru.entity;

import capstone.carru.entity.status.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_reservations")
@NoArgsConstructor
@Getter
public class ProductReservation extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_reservation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; //예약한 운송자

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; //예약한 운송자

    @Column(nullable = false)
    private ProductStatus productStatus; //예약 상태

    @Builder
    public ProductReservation(User user, Product product,
            ProductStatus productStatus) {
        this.user = user;
        this.product = product;
        this.productStatus = productStatus;
    }

    public void updateProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
}
