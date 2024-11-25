package capstone.carru.repository.productReservation;

import capstone.carru.entity.ProductReservation;
import capstone.carru.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReservationRepository extends JpaRepository<ProductReservation, Long>, ProductReservationRepositoryCustom {
    Optional<ProductReservation> findByProductIdAndDeletedDateIsNullAndUser(Long productId, User user);
    Optional<ProductReservation> findByProductIdAndDeletedDateIsNull(Long productId);
}
