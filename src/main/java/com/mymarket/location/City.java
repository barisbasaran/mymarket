package com.mymarket.location;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class City {
    Long id;
    String name;
}
