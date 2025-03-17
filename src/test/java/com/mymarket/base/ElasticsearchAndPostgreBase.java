package com.mymarket.base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

@Testcontainers
public class ElasticsearchAndPostgreBase extends PostgreBase {

    @Container
    public static ElasticsearchContainer elasticsearchContainer =
        new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.17.1")
            .withEnv("xpack.security.transport.ssl.enabled", "false")
            .withEnv("xpack.security.http.ssl.enabled", "false")
            .withPassword("bbmp1357");

    @BeforeAll
    public static void setUp() {
        elasticsearchContainer.addExposedPort(9200);
        elasticsearchContainer.setWaitStrategy(
            Wait.forHttp("/")
                .forPort(9200)
                .forStatusCode(200)
                .withStartupTimeout(Duration.ofSeconds(5)));

        elasticsearchContainer.start();
        assert elasticsearchContainer.isRunning();

        postgreContainer.start();
    }

    @AfterAll
    public static void tearDown() {
        elasticsearchContainer.stop();
        postgreContainer.stop();
    }

    @DynamicPropertySource
    static void elasticsearchProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.elasticsearch.repositories.enabled", () -> "true");
        registry.add("search.url", () -> elasticsearchContainer.getHttpHostAddress());
    }
}
