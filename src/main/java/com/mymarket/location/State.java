package com.mymarket.location;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class State {

    private Long id;

    private String name;
}
