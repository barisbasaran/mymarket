package com.mymarket.email;

import com.mymarket.base.PostgreBase;
import com.mymarket.base.TestElasticsearchConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestElasticsearchConfig.class)
class EmailServiceIT extends PostgreBase {

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
