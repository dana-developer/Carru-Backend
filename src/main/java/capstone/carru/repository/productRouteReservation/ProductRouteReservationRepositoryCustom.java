package capstone.carru.repository.productRouteReservation;

import capstone.carru.entity.ProductRouteReservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRouteReservationRepositoryCustom {
    Slice<ProductRouteReservation> getReservingList(Pageable pageable, String email, String listType);

    Slice<ProductRouteReservation> getApprovedListByDriver(Long userId, Pageable pageable);
}
