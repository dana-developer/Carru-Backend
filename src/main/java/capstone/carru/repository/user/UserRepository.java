package capstone.carru.repository.user;

import capstone.carru.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmailAndDeletedDateIsNull(String email);
    Optional<User> findByIdAndDeletedDateIsNullAndApprovedDateIsNull(Long id);
}
