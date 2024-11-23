package capstone.carru.repository.user;

import capstone.carru.entity.User;
import capstone.carru.entity.status.UserStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UserRepositoryCustom {
    Slice<User> getApprovingList(Pageable pageable, UserStatus userStatus);

    Slice<User> getApprovedList(Pageable pageable, UserStatus userStatus);
}
