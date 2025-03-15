package com.mymarket.location;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
    private CountryRepository countryRepository;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void getCountries() {
        countryRepository.deleteAll();
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
        assertThat(country.getCities().stream().filter(it -> it.getName().equals("Istanbul")).findFirst()).isPresent();
        assertThat(country.getCities().stream().filter(it -> it.getName().equals("Ankara")).findFirst()).isPresent();
        assertThat(country.isHasState()).isFalse();
        assertThat(country.getStates()).isEmpty();
    }

    @Test
    public void getCountry() {
        countryRepository.deleteAll();
        var createCountry = CreateCountry.builder()
            .name("country.germany")
            .hasState(false)
            .cities("Berlin\nMunich")
            .build();
        var countryCreated = countryService.createCountry(createCountry);

        var response = template.getForEntity("/locations/countries/" + countryCreated.getId(), Country.class);
        var country = response.getBody();
        assertThat(country).isNotNull();
        assertThat(country.getName()).isEqualTo("Germany");
        assertThat(country.getCities()).hasSize(2);
        assertThat(country.getCities().stream().filter(it -> it.getName().equals("Berlin")).findFirst()).isPresent();
        assertThat(country.getCities().stream().filter(it -> it.getName().equals("Munich")).findFirst()).isPresent();
        assertThat(country.isHasState()).isFalse();
        assertThat(country.getStates()).isEmpty();
    }
}
