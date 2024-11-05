package capstone.carru.repository;

import capstone.carru.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndDeletedDateIsNull(String email);
}
