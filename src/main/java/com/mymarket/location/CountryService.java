package com.mymarket.location;

import com.mymarket.web.error.ApplicationException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CountryService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final CountryMapper countryMapper;
    private final CityMapper cityMapper;
    private final StateMapper stateMapper;

    public Country createCountry(CreateCountry createCountry) {
        var cities = createCountry.getCities().trim();
        List<CityEntity> cityEntities = cities.isBlank() ? List.of() : Arrays.stream(cities.split("\n"))
            .map(cityName -> CityEntity.builder().name(cityName).build()).toList();
        var countryEntity = CountryEntity.builder()
            .name(createCountry.getName())
            .hasState(createCountry.isHasState())
            .cities(cityEntities)
            .build();
        countryEntity.getCities().forEach(cityEntity -> cityEntity.setCountry(countryEntity));

        try {
            var country = countryMapper.toDomain(countryRepository.save(countryEntity));
            log.info("country created {}", country);
            return country;
        } catch (DataIntegrityViolationException ex) {
            throw new ApplicationException("country-exits");
        }
    }

    public List<Country> getCountries() {
        return countryRepository.findAll().stream()
            .map(countryMapper::toDomain)
            .sorted(Comparator.comparing(Country::getName))
            .toList();
    }

    public Country getCountry(Long countryId) {
        return countryRepository.findById(countryId)
            .map(countryMapper::toDomain)
            .orElseThrow(() -> new ApplicationException("country-not-found"));
    }

    public City getCity(Long cityId) {
        return cityRepository.findById(cityId)
            .map(cityMapper::toDomain)
            .orElseThrow(() -> new ApplicationException("city-not-found"));
    }

    public State getState(Long stateId) {
        return stateRepository.findById(stateId)
            .map(stateMapper::toDomain)
            .orElseThrow(() -> new ApplicationException("state-not-found"));
    }
}
