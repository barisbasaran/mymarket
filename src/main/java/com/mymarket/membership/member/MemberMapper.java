package com.mymarket.membership.member;

import com.mymarket.membership.address.AddressMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberMapper {

    private final AddressMapper addressMapper;

    public MemberEntity toEntity(Member member) {
        if (member == null) {
            return null;
        }
        return MemberEntity.builder()
            .id(member.getId())
            .active(member.isActive())
            .firstName(member.getFirstName())
            .lastName(member.getLastName())
            .email(member.getEmail())
            .phone(member.getPhone())
            .roles(member.getRoles())
            .address(addressMapper.toEntity(member.getAddress()))
            .build();
    }

    public Member toDomain(MemberEntity memberEntity) {
        if (memberEntity == null) {
            return null;
        }
        return Member.builder()
            .id(memberEntity.getId())
            .active(memberEntity.isActive())
            .firstName(memberEntity.getFirstName())
            .lastName(memberEntity.getLastName())
            .email(memberEntity.getEmail())
            .phone(memberEntity.getPhone())
            .roles(memberEntity.getRoles())
            .address(addressMapper.toDomain(memberEntity.getAddress()))
            .build();
    }
}
