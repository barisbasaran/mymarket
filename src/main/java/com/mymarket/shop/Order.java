package com.mymarket.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mymarket.membership.member.Member;
import com.mymarket.product.Price;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Order {

    private Long id;

    private String name;

    private Member member;

    private Shipment shipment;

    private List<OrderItem> items;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateCreated;

    private Price totalPrice;
}
