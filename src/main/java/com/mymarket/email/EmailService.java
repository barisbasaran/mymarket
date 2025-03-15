package com.mymarket.email;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
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
