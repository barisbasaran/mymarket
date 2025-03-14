package com.mymarket.review;

import com.mymarket.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findByOrderItem_Product(ProductEntity orderItemProduct);

    Optional<ReviewEntity> findByOrderItem_Id(Long orderItemId);
}
