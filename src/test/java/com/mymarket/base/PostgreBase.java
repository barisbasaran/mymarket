package com.mymarket.base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgreBase {

    @Container
    public static PostgreSQLContainer<?> postgreContainer = new PostgreSQLContainer<>("postgres:17.2")
        .withDatabaseName("mydb2")
        .withUsername("postgres")
        .withPassword("sa");

    @BeforeAll
    public static void setUp() {
        postgreContainer.start();
    }

    @AfterAll
    public static void tearDown() {
        postgreContainer.stop();
    }
}
