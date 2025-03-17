package com.mymarket.review;

import com.mymarket.shop.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewMapper {

    private final OrderMapper orderMapper;

    public Review toDomain(ReviewEntity reviewEntity) {
        if (reviewEntity == null) {
            return null;
        }
        return Review.builder()
            .id(reviewEntity.getId())
            .rating(reviewEntity.getRating())
            .comment(reviewEntity.getComment())
            .orderItem(orderMapper.toOrderItemDomain(reviewEntity.getOrderItem()))
            .build();
    }
}
