package capstone.carru.repository;

import capstone.carru.entity.User;
import capstone.carru.entity.Warehouse;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findByNameContainingOrLocationContaining(String nameKeyword, String locationKeyword);
    Optional<Warehouse> findFirstByUserAndDeletedDateIsNull(User user);
}