package capstone.carru.repository.productRouteReservation;

import capstone.carru.entity.ProductRouteReservation;
import capstone.carru.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRouteReservationRepository extends JpaRepository<ProductRouteReservation, Long>, ProductRouteReservationRepositoryCustom {
    Optional<ProductRouteReservation> findByIdAndDeletedDateIsNullAndUser(Long id, User user);
    Optional<ProductRouteReservation> findByIdAndDeletedDateIsNull(Long id);
}
