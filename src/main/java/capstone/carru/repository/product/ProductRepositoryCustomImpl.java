package capstone.carru.repository.product;

import static capstone.carru.entity.QProduct.product;

import capstone.carru.entity.Product;
import capstone.carru.entity.status.ProductStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Product> getLogisticsMatchingListRequest(Pageable pageable,
            Long maxWeight, Long minWeight,
            int sortPrice, String warehouseKeyword, int sortOperationDistance) {
        List<Product> contents = queryFactory.selectFrom(product)
                .where(
                        (product.deletedDate.isNull())
                                .and(product.productStatus.eq(ProductStatus.valueOf("APPROVED")))
                                .and(product.weight.goe(minWeight))
                                .and(product.weight.loe(maxWeight))
                                .and(product.warehouse.location.startsWith(warehouseKeyword))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(sortPrice == 0 ? product.price.asc() : product.price.desc(),
                        sortOperationDistance == 0 ? product.operationDistance.asc() : product.operationDistance.desc())
                .fetch();

        return new SliceImpl<>(contents, pageable, hasNextPage(contents, pageable.getPageSize()));
    }

    @Override
    public Slice<Product> getApprovingList(Pageable pageable) {
        List<Product> contents = queryFactory.selectFrom(product)
                .where(
                        (product.deletedDate.isNull())
                                .and(product.productStatus.eq(ProductStatus.valueOf("WAITING")))
                                .and(product.approvedDate.isNull())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(product.createdDate.desc())
                .fetch();

        return new SliceImpl<>(contents, pageable, hasNextPage(contents, pageable.getPageSize()));
    }

    @Override
    public Slice<Product> getApprovedList(Pageable pageable) {
        List<Product> contents = queryFactory.selectFrom(product)
                .where(
                        (product.deletedDate.isNull())
                                .and(product.approvedDate.isNotNull())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(product.createdDate.desc())
                .fetch();

        return new SliceImpl<>(contents, pageable, hasNextPage(contents, pageable.getPageSize()));
    }

    @Override
    public Slice<Product> getApprovedListByUser(Long userId, Pageable pageable) {
        List<Product> contents = queryFactory.selectFrom(product)
                .where(
                        (product.deletedDate.isNull())
                                .and(product.approvedDate.isNotNull())
                                .and(product.warehouse.user.id.eq(userId))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(product.createdDate.desc())
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
