package capstone.carru.entity;

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
@Table(name = "stop_overs")
@NoArgsConstructor
@Getter
public class StopOver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stop_over_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; //운송할 물류

    @ManyToOne
    @JoinColumn(name = "product_route_reservation_id")
    private ProductRouteReservation productRouteReservation; //운송할 경로

    @Builder
    public StopOver(Product product, ProductRouteReservation productRouteReservation) {
        this.product = product;
        this.productRouteReservation = productRouteReservation;
    }
}
