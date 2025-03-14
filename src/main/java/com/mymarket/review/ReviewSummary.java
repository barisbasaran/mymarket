package com.mymarket.review;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReviewSummary {

    int rating;

    long count;
}
