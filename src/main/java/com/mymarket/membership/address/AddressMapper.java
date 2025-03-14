package com.mymarket.membership.address;

import com.mymarket.location.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressMapper {

    private final CountryService countryService;

    public AddressEntity toEntity(Address address) {
        if (address == null) {
            return null;
        }
        return AddressEntity.builder()
            .id(address.getId())
            .cityId(address.getCityId())
            .countryId(address.getCountryId())
            .stateId(address.getStateId())
            .postalCode(address.getPostalCode())
            .addressLine(address.getAddressLine())
            .build();
    }

    public Address toDomain(AddressEntity addressEntity) {
        if (addressEntity == null) {
            return null;
        }
        var country = countryService.getCountry(addressEntity.getCountryId());
        var city = countryService.getCity(addressEntity.getCityId());
        var stateName = addressEntity.getStateId() != null ? countryService.getState(addressEntity.getStateId()).getName() : "";
        return Address.builder()
            .id(addressEntity.getId())
            .cityId(addressEntity.getCityId())
            .countryId(addressEntity.getCountryId())
            .stateId(addressEntity.getStateId())
            .postalCode(addressEntity.getPostalCode())
            .addressLine(addressEntity.getAddressLine())
            .countryName(country.getName())
            .hasState(country.isHasState())
            .cityName(city.getName())
            .stateName(stateName)
            .build();
    }
}
