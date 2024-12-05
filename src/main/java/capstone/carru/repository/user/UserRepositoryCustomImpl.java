package capstone.carru.repository.user;

import static capstone.carru.entity.QUser.user;

import capstone.carru.entity.User;
import capstone.carru.entity.status.UserStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<User> getApprovingList(Pageable pageable, UserStatus userStatus) {
        List<User> contents = queryFactory.selectFrom(user)
                .where(
                        (user.deletedDate.isNull())
                                .and(user.userStatus.eq(userStatus))
                                .and(user.approvedDate.isNull())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(user.createdDate.desc())
                .fetch();

        return new SliceImpl<>(contents, pageable, hasNextPage(contents, pageable.getPageSize()));
    }

    @Override
    public Slice<User> getApprovedList(Pageable pageable, UserStatus userStatus) {
        List<User> contents = queryFactory.selectFrom(user)
                .where(
                        (user.deletedDate.isNull())
                                .and(user.userStatus.eq(userStatus))
                                .and(user.approvedDate.isNotNull())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(user.createdDate.desc())
                .fetch();

        return new SliceImpl<>(contents, pageable, hasNextPage(contents, pageable.getPageSize()));
    }

    private boolean hasNextPage(List<User> contents, int pageSize) {
        if (contents.size() > pageSize) {
            contents.remove(pageSize);
            return true;
        }
        return false;
    }
}
