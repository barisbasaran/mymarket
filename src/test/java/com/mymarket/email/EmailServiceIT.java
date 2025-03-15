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
    public void findAll() {
        emailService.saveEmailRequest("myemail@gmail.com", "My Email", "Hello World!");

        var emailEntities = emailRepository.findAll();
        assertThat(emailEntities.size()).isEqualTo(1);
        var emailEntity = emailEntities.getFirst();
        assertThat(emailEntity.getRecipient()).isEqualTo("myemail@gmail.com");
        assertThat(emailEntity.getSubject()).isEqualTo("My Email");
        assertThat(emailEntity.getContent()).isEqualTo("Hello World!");
        assertThat(emailEntity.getId()).isNotNull();
        assertThat(emailEntity.getCreated()).isNotNull();
        assertThat(emailEntity.getRetry()).isEqualTo(0L);
    }
}
