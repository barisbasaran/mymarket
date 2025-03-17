package com.mymarket.base;

import com.mymarket.search.SProductRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestElasticsearchConfig {

    @Bean
    public SProductRepository SProductRepository() {
        return Mockito.mock(SProductRepository.class);
    }
}
