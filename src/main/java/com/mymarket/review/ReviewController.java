package com.mymarket.review;

import com.mymarket.shop.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewController {

    private ReviewService reviewService;
    private OrderService orderService;

    @GetMapping("/products/{productId}")
    public List<Review> getProductReviews(@PathVariable Long productId) {
        return reviewService.getProductReviews(productId);
    }

    @PostMapping("/orders/{orderName}")
    @SneakyThrows
    public ResponseEntity<Review> createReview(
        @PathVariable String orderName,
        @Valid @RequestBody Review review
    ) {
        // validate order exists
        orderService.getMyOrder(orderName);
        review.setId(null);

        var reviewCreated = reviewService.createReview(review);

        var uri = new URI("/reviews/" + reviewCreated.getId());
        return ResponseEntity.created(uri).body(reviewCreated);
    }
}
