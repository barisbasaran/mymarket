package com.mymarket.shop;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Shipment {

    private Long id;

    @Email(message = "{error.email.format}")
    @Size(min = 3, max = 100, message = "{error.email}")
    private String email;

    @NotNull(message = "{error.first-name}")
    @Size(min = 1, max = 100, message = "{error.first-name}")
    private String firstName;

    @NotNull(message = "{error.last-name}")
    @Size(min = 1, max = 100, message = "{error.last-name}")
    private String lastName;

    @Size(max = 20, message = "{error.shipment.phone}")
    private String phone;

    @NotNull(message = "{error.shipment.cityId}")
    private Long cityId;

    @NotNull(message = "{error.shipment.countryId}")
    private Long countryId;

    private Long stateId;

    @Size(max = 20, message = "{error.shipment.postalCode}")
    private String postalCode;

    @NotNull(message = "{error.shipment.addressLine}")
    @Size(min = 1, max = 300, message = "{error.shipment.addressLine}")
    private String addressLine;

    private String cityName;

    private String stateName;

    private String countryName;

    public String getName() {
        var sb = new StringBuilder();
        if (firstName != null) sb.append(firstName).append(" ");
        if (lastName != null) sb.append(lastName);
        return sb.toString();
    }
}
