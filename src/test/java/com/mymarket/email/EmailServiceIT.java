package com.mymarket.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class EmailServiceIT {

    @Container
    public static PostgreSQLContainer<?> postgreContainer = new PostgreSQLContainer<>("postgres:17.2")
        .withDatabaseName("mydb2")
        .withUsername("postgres")
        .withPassword("sa");

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailRepository emailRepository;

    @Test
    public void testFindAll() {
        emailService.saveEmailRequest("myemail@gmail.com", "My Email", "Hello World!");

        var emailEntities = emailRepository.findAll();
        assertThat(emailEntities.size()).isEqualTo(1);
    }
}
