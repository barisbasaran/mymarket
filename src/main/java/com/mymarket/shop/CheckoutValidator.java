package com.mymarket.shop;

import com.mymarket.location.CountryService;
import com.mymarket.web.error.ApplicationValidator;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@AllArgsConstructor
@Slf4j
public class CheckoutValidator implements ApplicationValidator<Checkout> {

    private final CountryService countryService;
    @Getter
    private final Validator validator;

    @Override
    public void addCustomErrors(Checkout checkout, BindingResult bindingResult) {
        var shipment = checkout.getShipment();
        if (shipment.getCountryId() != null) {
            var country = countryService.getCountry(shipment.getCountryId());
            if (country.isHasState() && shipment.getStateId() == null) {
                bindingResult.rejectValue("shipment.stateId", "", "{error.stateId}");
            }
        }
    }
}
