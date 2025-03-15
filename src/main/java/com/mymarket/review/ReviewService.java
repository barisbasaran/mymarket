package com.mymarket.review;

import com.mymarket.product.ProductEntity;
import com.mymarket.shop.OrderItemRepository;
import com.mymarket.web.error.ApplicationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final OrderItemRepository orderItemRepository;

    public List<Review> getProductReviews(Long productId) {
        var productEntity = ProductEntity.builder().id(productId).build();
        return reviewRepository.findByOrderItem_Product(productEntity).stream()
            .map(reviewMapper::toDomain)
            .toList();
    }

    public Review createReview(Review review) {
        var orderItemEntity = orderItemRepository.findById(review.getOrderItem().getId())
            .orElseThrow(() -> new ApplicationException("order-not-found"));

        var reviewEntity = ReviewEntity.builder()
            .rating(review.getRating())
            .comment(review.getComment())
            .orderItem(orderItemEntity)
            .build();
        var createdReview = reviewMapper.toDomain(reviewRepository.save(reviewEntity));

        log.info("review created {}", createdReview);
        return createdReview;
    }
}
