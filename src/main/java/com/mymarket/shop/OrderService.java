package com.mymarket.shop;

import com.mymarket.email.EmailService;
import com.mymarket.localization.TranslationService;
import com.mymarket.membership.member.Member;
import com.mymarket.membership.member.MemberMapper;
import com.mymarket.product.ProductEntity;
import com.mymarket.product.ProductService;
import com.mymarket.review.ReviewEntity;
import com.mymarket.review.ReviewRepository;
import com.mymarket.web.error.ApplicationException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class OrderService {

    private final TranslationService translationService;
    private final EmailService emailService;
    private final OrderRepository orderRepository;
    private final ShipmentRepository shipmentRepository;
    private final OrderMapper orderMapper;
    private final ShipmentMapper shipmentMapper;
    private final MemberMapper memberMapper;
    private final ProductService productService;
    private final ReviewRepository reviewRepository;
    private final String baseUrl;

    public OrderService(
        TranslationService translationService, EmailService emailService, OrderRepository orderRepository,
        ShipmentRepository shipmentRepository, OrderMapper orderMapper, ShipmentMapper shipmentMapper,
        MemberMapper memberMapper, ProductService productService, ReviewRepository reviewRepository,
        @Value("${app.base.url}") String baseUrl
    ) {
        this.translationService = translationService;
        this.emailService = emailService;
        this.orderRepository = orderRepository;
        this.shipmentRepository = shipmentRepository;
        this.orderMapper = orderMapper;
        this.shipmentMapper = shipmentMapper;
        this.memberMapper = memberMapper;
        this.productService = productService;
        this.reviewRepository = reviewRepository;
        this.baseUrl = baseUrl;
    }

    public Order createOrder(Checkout checkout, Member member) {
        var shipmentEntity = shipmentMapper.toEntity(checkout.getShipment());
        shipmentEntity = shipmentRepository.save(shipmentEntity);

        var orderEntity = OrderEntity.builder()
            .name(UUID.randomUUID().toString())
            .shipment(shipmentEntity)
            .items(checkout.getItems().stream().map(cartItem -> {
                var product = productService.getProduct(cartItem.getProductId());
                return OrderItemEntity.builder()
                    .product(ProductEntity.builder().id(cartItem.getProductId()).build())
                    .quantity(cartItem.getQuantity())
                    .priceAmount(product.getPrice().getAmount())
                    .priceCurrency(product.getPrice().getCurrency())
                    .build();
            }).toList())
            .member(memberMapper.toEntity(member))
            .dateCreated(LocalDateTime.now())
            .build();
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));

        var order = orderMapper.toOrder(orderRepository.save(orderEntity));
        log.info("Order created: {}", order);

        var content = translationService.translate("order-created-email", new String[]{baseUrl, order.getName()});
        var subject = translationService.translate("order-created-email-subject");
        emailService.saveEmailRequest(checkout.getShipment().getEmail(), subject, content);

        return order;
    }

    public List<Order> getMyOrders(Long memberId) {
        return orderRepository.findByMemberId(memberId).stream()
            .map(orderMapper::toOrder)
            .toList();
    }

    public Order getMyOrder(String name) {
        var order = orderRepository.findByName(name)
            .map(orderMapper::toOrder)
            .orElseThrow(() -> new ApplicationException("order-not-found"));
        order.getItems().forEach(orderItem -> {
            var review = reviewRepository.findByOrderItem_Id(orderItem.getId());
            orderItem.setReviewRating(review.map(ReviewEntity::getRating).orElse(0));
        });
        return order;
    }
}
