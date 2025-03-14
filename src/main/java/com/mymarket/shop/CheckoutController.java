package com.mymarket.shop;

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
@RequestMapping("/shop/checkout")
@AllArgsConstructor
public class CheckoutController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final CheckoutValidator checkoutValidator;

    @PostMapping
    @SneakyThrows
    public ResponseEntity<Order> checkout(@RequestBody Checkout checkout, BindingResult bindingResult) {
        checkoutValidator.validate(checkout, bindingResult);

        // clearing shipment id
        checkout.getShipment().setId(null);

        var currentMember = memberService.getCurrentMember();
        // override email if user is logged in
        currentMember.ifPresent(member ->
            checkout.getShipment().setEmail(member.getEmail()));

        var order = orderService.createOrder(checkout, currentMember.orElse(null));
        var uri = new URI("/service/orders/" + order.getId());
        return ResponseEntity.created(uri).body(order);
    }
}
