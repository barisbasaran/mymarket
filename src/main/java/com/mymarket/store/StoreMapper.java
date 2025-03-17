package com.mymarket.store;

import com.mymarket.membership.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreMapper {

    private final MemberMapper memberMapper;

    public Store toDomain(StoreEntity storeEntity) {
        if (storeEntity == null) {
            return null;
        }
        return Store.builder()
            .id(storeEntity.getId())
            .name(storeEntity.getName())
            .member(memberMapper.toDomain(storeEntity.getMember()))
            .build();
    }

    public StoreEntity toEntity(Store store) {
        if (store == null) {
            return null;
        }
        return StoreEntity.builder()
            .id(store.getId())
            .name(store.getName())
            .member(memberMapper.toEntity(store.getMember()))
            .build();
    }
}
