package com.mymarket.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.time.Duration;

@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.mymarket.search")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    private final String username;
    private final String password;
    private final String url;

    public ElasticsearchConfig(
        @Value("${search.username}") String username,
        @Value("${search.password}") String password,
        @Value("${search.url}") String url
    ) {
        this.username = username;
        this.password = password;
        this.url = url;
    }

    @Override
    public ClientConfiguration clientConfiguration() {

        return ClientConfiguration.builder()
            .connectedTo(url)
            .withBasicAuth(username, password)
            .withConnectTimeout(Duration.ofSeconds(3))
            .withSocketTimeout(Duration.ofSeconds(5))
            /*.withHeaders(() -> {
                var httpHeaders = new HttpHeaders();
                httpHeaders.add("Authorization", "ApiKey " + apiKey);
                return httpHeaders;
            })*/
            .build();
    }
}
