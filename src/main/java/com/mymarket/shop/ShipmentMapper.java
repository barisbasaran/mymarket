package com.mymarket.shop;

import com.mymarket.location.CountryService;
import com.mymarket.location.State;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ShipmentMapper {

    private final CountryService countryService;

    public ShipmentEntity toEntity(Shipment shipment) {
        return ShipmentEntity.builder()
            .id(shipment.getId())
            .email(shipment.getEmail())
            .firstName(shipment.getFirstName())
            .lastName(shipment.getLastName())
            .phone(shipment.getPhone())
            .addressLine(shipment.getAddressLine())
            .cityId(shipment.getCityId())
            .countryId(shipment.getCountryId())
            .stateId(shipment.getStateId())
            .postalCode(shipment.getPostalCode())
            .build();
    }

    public Shipment toDomain(ShipmentEntity shipmentEntity) {
        var country = countryService.getCountry(shipmentEntity.getCountryId());
        var city = countryService.getCity(shipmentEntity.getCityId());
        var stateId = shipmentEntity.getStateId();
        Optional<State> state = stateId != null ? country.getStates().stream()
            .filter(it -> it.getId().equals(stateId)).findFirst() : Optional.empty();
        return Shipment.builder()
            .id(shipmentEntity.getId())
            .email(shipmentEntity.getEmail())
            .firstName(shipmentEntity.getFirstName())
            .lastName(shipmentEntity.getLastName())
            .phone(shipmentEntity.getPhone())
            .addressLine(shipmentEntity.getAddressLine())
            .cityId(shipmentEntity.getCityId())
            .countryId(shipmentEntity.getCountryId())
            .stateId(stateId)
            .postalCode(shipmentEntity.getPostalCode())
            .countryName(country.getName())
            .cityName(city.getName())
            .stateName(state.map(State::getName).orElse(""))
            .build();
    }
}
