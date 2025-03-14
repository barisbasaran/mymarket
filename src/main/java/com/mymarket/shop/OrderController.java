package com.mymarket.shop;

import com.mymarket.membership.member.MemberService;
import com.mymarket.membership.member.MemberNotLoggedInException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shop/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;

    @GetMapping("/{name}")
    public Order getMyOrder(@PathVariable String name) {
        return orderService.getMyOrder(name);
    }

    @GetMapping
    public List<Order> getMyOrders() {
        var currentMember = memberService.getCurrentMember()
            .orElseThrow(MemberNotLoggedInException::new);
        return orderService.getMyOrders(currentMember.getId());
    }
}
