package com.mymarket.membership.address;

import com.mymarket.membership.member.MemberNotLoggedInException;
import com.mymarket.membership.member.MemberService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/addresses/self")
@AllArgsConstructor
public class AddressController {

    private final MemberService memberService;
    private final AddressService addressService;
    private final AddressValidator addressValidator;

    @PostMapping
    @SneakyThrows
    public ResponseEntity<Address> updateAddress(@RequestBody Address address, BindingResult bindingResult) {
        addressValidator.validate(address, bindingResult);

        var currentMember = memberService.getCurrentMember()
            .orElseThrow(MemberNotLoggedInException::new);

        var resultingAddress = currentMember.getAddress() == null ?
            addressService.createAddress(currentMember, address)
            : addressService.updateAddress(currentMember, address);
        return ResponseEntity.created(new URI("/")).body(resultingAddress);
    }
}
