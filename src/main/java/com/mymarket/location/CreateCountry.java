package com.mymarket.location;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCountry {

    @Size(min = 1, max = 50, message = "{error.location.country.name}")
    private String name;

    private boolean hasState;

    @Size(max = 100000, message = "{error.location.country.cities}")
    private String cities;
}
