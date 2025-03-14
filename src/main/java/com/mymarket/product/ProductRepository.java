package com.mymarket.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByProductCategoryId(Long categoryId);

    List<ProductEntity> findByStore_Id(Long storeId);
}
