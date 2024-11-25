package capstone.carru.repository.productReservation;

import static capstone.carru.entity.QProduct.product;
import static capstone.carru.entity.QProductReservation.productReservation;

import capstone.carru.entity.Product;
import capstone.carru.entity.status.ProductStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class ProductReservationRepositoryCustomImpl implements ProductReservationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Product> getReservingList(Pageable pageable, String email, String listType) {
        List<Product> contents = queryFactory.selectFrom(product)
                .join(productReservation).on(productReservation.product.id.eq(product.id))
                .where(
                        (productReservation.deletedDate.isNull())
                                .and(productReservation.productStatus.eq(ProductStatus.valueOf(listType)))
                                .and(productReservation.user.email.eq(email))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(productReservation.createdDate.desc())
                .fetch();

        return new SliceImpl<>(contents, pageable, hasNextPage(contents, pageable.getPageSize()));
    }

    @Override
    public Slice<Product> getApprovedListByDriver(Long userId, Pageable pageable) {
        List<Product> contents = queryFactory.selectFrom(product)
                .join(productReservation).on(productReservation.product.id.eq(product.id))
                .where(
                        (productReservation.deletedDate.isNull())
                                .and(productReservation.product.approvedDate.isNotNull())
                                .and(productReservation.user.id.eq(userId))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(productReservation.createdDate.desc())
                .fetch();

        return new SliceImpl<>(contents, pageable, hasNextPage(contents, pageable.getPageSize()));
    }

    private boolean hasNextPage(List<Product> contents, int pageSize) {
        if (contents.size() > pageSize) {
            contents.remove(pageSize);
            return true;
        }
        return false;
    }
}
