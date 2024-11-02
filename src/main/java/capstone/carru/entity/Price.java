package capstone.carru.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prices")
@NoArgsConstructor
public class Price extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long id;

    @Column(nullable = false)
    private Long distance;

    @Column(nullable = false)
    private Long weight;

    @Column(nullable = false)
    private Long price;
}
