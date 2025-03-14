package com.mymarket.membership.address;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {

    private Long id;

    @NotNull(message = "{error.address.cityId}")
    private Long cityId;

    @NotNull(message = "{error.address.countryId}")
    private Long countryId;

    private Long stateId;

    @NotNull(message = "{error.address.postalCode}")
    @Size(max = 20, message = "{error.address.postalCode}")
    private String postalCode;

    @NotNull(message = "{error.address.addressLine}")
    @Size(min = 1, max = 300, message = "{error.address.addressLine}")
    private String addressLine;

    private String countryName;

    private String cityName;

    private String stateName;

    private boolean hasState;
}
