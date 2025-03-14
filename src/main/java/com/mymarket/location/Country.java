package com.mymarket.location;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Country {
    Long id;
    String name;
    List<City> cities;
    boolean hasState;
    List<State> states;
}
