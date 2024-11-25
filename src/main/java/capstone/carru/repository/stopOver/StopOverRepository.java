package capstone.carru.repository.stopOver;

import capstone.carru.entity.ProductRouteReservation;
import capstone.carru.entity.StopOver;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StopOverRepository extends JpaRepository<StopOver, Long> {
    List<StopOver> findByProductRouteReservation(ProductRouteReservation productRouteReservation);

    Optional<StopOver> findFirstByProductId(Long productId);
}
