package com.mymarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@SpringBootApplication
@EnableScheduling
@EnableWebMvc
public class MyMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyMarketApplication.class, args);
    }

    @Bean
    public AcceptHeaderLocaleResolver acceptHeaderLocaleResolver() {
        var localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        Locale.setDefault(Locale.US);
        return localeResolver;
    }
}
