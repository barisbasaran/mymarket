package com.mymarket.shop;

import com.mymarket.membership.member.MemberMapper;
import com.mymarket.product.Currency;
import com.mymarket.product.Price;
import com.mymarket.product.ProductMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class OrderMapper {

    private final MemberMapper memberMapper;
    private final ShipmentMapper shipmentMapper;
    private final ProductMapper productMapper;

    public Order toOrder(OrderEntity orderEntity) {
        var totalPriceAmount = orderEntity.getItems().stream()
            .map(item -> item.getPriceAmount() == null ?
                BigDecimal.ZERO : item.getPriceAmount().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        var totalPriceAmountCurrency = Currency.TL;
        if (!orderEntity.getItems().isEmpty()) {
            totalPriceAmountCurrency = orderEntity.getItems().getFirst().getPriceCurrency();
        }
        return Order.builder()
            .id(orderEntity.getId())
            .name(orderEntity.getName())
            .member(memberMapper.toDomain(orderEntity.getMember()))
            .shipment(shipmentMapper.toDomain(orderEntity.getShipment()))
            .items(orderEntity.getItems().stream()
                .map(this::toOrderItemDomain)
                .toList())
            .dateCreated(orderEntity.getDateCreated())
            .totalPrice(Price.builder()
                .amount(totalPriceAmount)
                .currency(totalPriceAmountCurrency)
                .build())
            .build();
    }

    public OrderItem toOrderItemDomain(OrderItemEntity orderItemEntity) {
        return OrderItem.builder()
            .id(orderItemEntity.getId())
            .product(productMapper.toDomain(orderItemEntity.getProduct()))
            .quantity(orderItemEntity.getQuantity())
            .price(Price.builder()
                .amount(orderItemEntity.getPriceAmount())
                .currency(orderItemEntity.getPriceCurrency())
                .build())
            .build();
    }
}
