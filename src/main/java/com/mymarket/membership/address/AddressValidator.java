package com.mymarket.membership.address;

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
public class AddressValidator implements ApplicationValidator<Address> {

    private final CountryService countryService;
    @Getter
    private final Validator validator;

    @Override
    public void addCustomErrors(Address address, BindingResult bindingResult) {
        var country = countryService.getCountry(address.getCountryId());
        if (country.isHasState() && address.getStateId() == null) {
            bindingResult.rejectValue("stateId", "", "{error.stateId}");
        }
    }
}
