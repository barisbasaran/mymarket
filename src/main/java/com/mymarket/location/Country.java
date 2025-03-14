package com.mymarket.location;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Country {

    private Long id;

    private String name;

    private List<City> cities;

    private boolean hasState;

    private List<State> states;
}
