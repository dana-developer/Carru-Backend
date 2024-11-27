package capstone.carru.repository.productReservation;

import capstone.carru.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductReservationRepositoryCustom {
    Slice<Product> getReservingList(Pageable pageable, String email, String listType);

    Slice<Product> getApprovedListByDriver(Long userId, Pageable pageable);
}
