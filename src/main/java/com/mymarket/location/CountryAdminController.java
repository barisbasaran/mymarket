package com.mymarket.location;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/admin/locations/countries")
@RequiredArgsConstructor
public class CountryAdminController {

    private final CountryService countryService;

    @PostMapping
    @SneakyThrows
    public ResponseEntity<Country> createCountry(@Valid @RequestBody CreateCountry createCountry) {
        var country = countryService.createCountry(createCountry);
        var uri = new URI("/service/locations/countries/" + country.getId());
        return ResponseEntity.created(uri).body(country);
    }
}
