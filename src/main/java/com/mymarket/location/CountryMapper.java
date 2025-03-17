package com.mymarket.location;

import com.mymarket.web.ApplicationLocaleHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryMapper {

    private final CityMapper cityMapper;
    private final StateMapper stateMapper;
    private final MessageSource messageSource;

    public Country toDomain(CountryEntity countryEntity) {
        if (countryEntity == null) {
            return null;
        }
        List<State> states = countryEntity.getStates() == null ? List.of() : countryEntity.getStates().stream()
            .map(stateMapper::toDomain)
            .sorted(Comparator.comparing(State::getName))
            .toList();
        List<City> cities = countryEntity.getCities() == null ? List.of() : countryEntity.getCities().stream()
            .map(cityMapper::toDomain)
            .sorted(Comparator.comparing(City::getName))
            .toList();
        return Country.builder()
            .id(countryEntity.getId())
            .name(messageSource.getMessage(countryEntity.getName(), null, ApplicationLocaleHolder.getLocale()))
            .hasState(countryEntity.isHasState())
            .cities(cities)
            .states(states)
            .hasState(countryEntity.isHasState())
            .build();
    }
}
