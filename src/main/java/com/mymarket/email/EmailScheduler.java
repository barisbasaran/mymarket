package com.mymarket.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailScheduler {

    private final EmailRepository emailRepository;
    private final EmailSender emailSender;

    @Scheduled(fixedDelay = 60000)
    public void sendEmails() {
        var emails = emailRepository.findByRetryLessThan(10);
        log.info("Found {} emails to be sent", emails.size());

        var eligibleEmails = emails.stream().filter(this::isEligible).toList();
        log.info("{} of {} the emails are eligible", eligibleEmails.size(), emails.size());

        eligibleEmails.forEach(email -> {
            var recipient = email.getRecipient();
            var subject = email.getSubject();
            var retry = email.getRetry();
            try {
                log.info("Sending email to {} with subject {}", recipient, subject);
                emailSender.sendEmail(recipient, subject, email.getContent());
                emailRepository.delete(email);
                log.info("Sent email to {} with subject {}", recipient, subject);
            } catch (Exception ex) {
                log.error("Error sending email at retry %d, to %s, with subject %s".formatted(retry, recipient, subject), ex);
                email.setRetry(retry + 1);
                emailRepository.save(email);
            }
        });
        if (!eligibleEmails.isEmpty()) {
            log.info("Finished sending {} emails", eligibleEmails.size());
        }
    }

    private boolean isEligible(EmailEntity emailEntity) {
        if (emailEntity.getRetry() == 0) return true;

        var now = LocalDateTime.now();
        var minutes = (long) Math.pow(2, emailEntity.getRetry() - 1) * 10;
        var dateTime = emailEntity.getCreated().plusMinutes(minutes);
        return now.isAfter(dateTime);
    }
}
