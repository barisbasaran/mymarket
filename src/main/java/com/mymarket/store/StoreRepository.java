package com.mymarket.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    Optional<StoreEntity> findByIdAndMemberId(Long storeId, Long memberId);

    List<StoreEntity> findByMemberId(Long memberId);
}
