package capstone.carru.repository.productRouteReservation;

import static capstone.carru.entity.QProductRouteReservation.productRouteReservation;

import capstone.carru.entity.ProductRouteReservation;
import capstone.carru.entity.status.ProductStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class ProductRouteReservationRepositoryCustomImpl implements
        ProductRouteReservationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<ProductRouteReservation> getReservingList(Pageable pageable, String email, String listType) {
        List<ProductRouteReservation> contents = queryFactory.selectFrom(productRouteReservation)
                .where(
                        (productRouteReservation.deletedDate.isNull())
                                .and(productRouteReservation.productStatus.eq(ProductStatus.valueOf(listType)))
                                .and(productRouteReservation.user.email.eq(email))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(productRouteReservation.createdDate.desc())
                .fetch();

        return new SliceImpl<>(contents, pageable, hasNextPage(contents, pageable.getPageSize()));
    }

    @Override
    public Slice<ProductRouteReservation> getApprovedListByDriver(Long userId, Pageable pageable) {
        List<ProductRouteReservation> contents = queryFactory.selectFrom(productRouteReservation)
                .where(
                        (productRouteReservation.deletedDate.isNull())
                                .and(productRouteReservation.user.id.eq(userId))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(productRouteReservation.createdDate.desc())
                .fetch();

        return new SliceImpl<>(contents, pageable, hasNextPage(contents, pageable.getPageSize()));
    }

    private boolean hasNextPage(List<ProductRouteReservation> contents, int pageSize) {
        if (contents.size() > pageSize) {
            contents.remove(pageSize);
            return true;
        }
        return false;
    }
}
