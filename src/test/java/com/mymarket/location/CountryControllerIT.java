package com.mymarket.location;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class CountryControllerIT {

    @Container
    public static PostgreSQLContainer<?> postgreContainer = new PostgreSQLContainer<>("postgres:17.2")
        .withDatabaseName("mydb2")
        .withUsername("postgres")
        .withPassword("sa");

    @Autowired
    private CountryService countryService;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void getHello() {
        LocaleContextHolder.setLocale(Locale.US);
        var createCountry = CreateCountry.builder()
            .name("country.turkey")
            .hasState(false)
            .cities("Istanbul\nAnkara")
            .build();
        countryService.createCountry(createCountry);

        var response = template.getForEntity("/locations/countries", Country[].class);
        var countries = response.getBody();
        assertThat(countries).isNotEmpty();
        assertThat(countries.length).isEqualTo(1);
        var country = countries[0];
        assertThat(country.getName()).isEqualTo("Turkey");
        assertThat(country.getCities()).hasSize(2);
        assertThat(country.getCities()).contains(City.builder().id(1L).name("Istanbul").build());
        assertThat(country.getCities()).contains(City.builder().id(2L).name("Ankara").build());
        assertThat(country.isHasState()).isFalse();
        assertThat(country.getStates()).isEmpty();
    }
}
