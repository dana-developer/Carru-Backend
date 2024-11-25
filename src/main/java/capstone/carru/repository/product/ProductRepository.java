package capstone.carru.repository.product;

import capstone.carru.entity.Product;
import java.util.Optional;
import java.util.List;

import capstone.carru.entity.User;
import capstone.carru.entity.status.ProductStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Optional<Product> findByIdAndDeletedDateIsNullAndApprovedDateIsNotNull(Long id);

    Slice<Product> getApprovingList(Pageable pageable);

    Optional<Product> findByIdAndDeletedDateIsNullAndApprovedDateIsNull(Long id);

    Optional<List<Product>> findAllByWarehouse_UserAndProductStatusAndDeletedDateIsNull(User user, ProductStatus status);

    Optional<Product> findByIdAndDeletedDateIsNullAndProductStatus(Long id, ProductStatus status);

    Optional<Product> findByIdAndWarehouseUserEmailAndDeletedDateIsNullAndApprovedDateIsNull(Long id, String email);
}
