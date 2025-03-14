package com.mymarket.email;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {

    private final EmailRepository emailRepository;

    public void saveEmailRequest(String recipient, String subject, String content) {
        var emailEntity = EmailEntity.builder()
            .recipient(recipient)
            .subject(subject)
            .content(content)
            .created(LocalDateTime.now())
            .build();

        emailEntity = emailRepository.save(emailEntity);

        log.info("Email request saved {}", emailEntity);
    }
}
