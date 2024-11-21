package capstone.carru.repository.product;

import capstone.carru.entity.Product;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Optional<Product> findByIdAndDeletedDateIsNullAndApprovedDateIsNotNull(Long id);

    Slice<Product> getApprovingList(Pageable pageable);
}
