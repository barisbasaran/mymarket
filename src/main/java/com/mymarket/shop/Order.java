package com.mymarket.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mymarket.membership.member.Member;
import com.mymarket.product.Price;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class Order {
    Long id;
    String name;
    Member member;
    Shipment shipment;
    List<OrderItem> items;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime dateCreated;
    Price totalPrice;
}
