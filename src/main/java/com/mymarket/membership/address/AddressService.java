package com.mymarket.membership.address;


import com.mymarket.membership.member.Member;
import com.mymarket.membership.member.MemberNotFoundException;
import com.mymarket.membership.member.MemberRepository;
import com.mymarket.web.error.ApplicationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final MemberRepository memberRepository;

    public Address createAddress(Member member, Address address) {
        var addressEntity = AddressEntity.builder().build();
        addressEntity.setCountryId(address.getCountryId());
        addressEntity.setCityId(address.getCityId());
        addressEntity.setStateId(address.getStateId());
        addressEntity.setAddressLine(address.getAddressLine());
        addressEntity.setPostalCode(address.getPostalCode());
        var createdAddressEntity = addressRepository.save(addressEntity);

        var memberEntity = memberRepository.findById(member.getId())
            .orElseThrow(MemberNotFoundException::new);
        memberEntity.setAddress(createdAddressEntity);
        memberRepository.save(memberEntity);

        var createdAddress = addressMapper.toDomain(createdAddressEntity);
        log.info("address created {}", createdAddress);
        return createdAddress;
    }

    public Address updateAddress(Member member, Address address) {
        var addressEntity = addressRepository.findById(member.getAddress().getId())
            .orElseThrow(() -> new ApplicationException("address-not-found"));

        addressEntity.setCountryId(address.getCountryId());
        addressEntity.setCityId(address.getCityId());
        addressEntity.setStateId(address.getStateId());
        addressEntity.setAddressLine(address.getAddressLine());
        addressEntity.setPostalCode(address.getPostalCode());
        var updatedAddress = addressMapper.toDomain(addressRepository.save(addressEntity));

        log.info("address updated {}", updatedAddress);
        return updatedAddress;
    }
}
