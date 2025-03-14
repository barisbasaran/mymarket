package com.mymarket.membership.login;

import com.mymarket.email.EmailService;
import com.mymarket.membership.member.Member;
import com.mymarket.membership.member.MemberEntity;
import com.mymarket.membership.member.MemberMapper;
import com.mymarket.membership.member.MemberNotFoundException;
import com.mymarket.membership.member.MemberRepository;
import com.mymarket.membership.member.Register;
import com.mymarket.web.ApplicationLocaleHolder;
import com.mymarket.web.error.ApplicationException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class PasswordService {

    private final MessageSource messageSource;
    private final PasswordResetRepository passwordResetRepository;
    private final VerifyEmailRepository verifyEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final String baseUrl;

    public PasswordService(
        MessageSource messageSource, PasswordResetRepository passwordResetRepository, VerifyEmailRepository verifyEmailRepository,
        PasswordEncoder passwordEncoder, EmailService emailService, MemberRepository memberRepository, MemberMapper memberMapper,
        @Value("${app.base.url}") String baseUrl
    ) {
        this.messageSource = messageSource;
        this.passwordResetRepository = passwordResetRepository;
        this.verifyEmailRepository = verifyEmailRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.baseUrl = baseUrl;
    }

    public Member register(Register register) {
        var memberEntity = MemberEntity.builder()
            .email(register.getEmail())
            .password(passwordEncoder.encode(register.getPassword()))
            .roles(List.of(register.getRole()))
            .active(false)
            .build();
        try {
            memberRepository.save(memberEntity);
            sendVerifyEmail(memberEntity);
            return memberMapper.toDomain(memberEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new ApplicationException("email-exists");
        }
    }

    public void verifyEmail(String token) {
        var verifyEmailEntity = verifyEmailRepository.findByToken(token)
            .orElseThrow(() -> new ApplicationException("token-not-found"));

        var memberEntity = verifyEmailEntity.getMember();
        memberEntity.setActive(true);
        memberRepository.save(memberEntity);

        verifyEmailRepository.delete(verifyEmailEntity);
    }

    public void changePassword(Long memberId, ChangePassword changePassword) {
        var memberEntity = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        changePassword(memberEntity, changePassword.getPassword1());
    }

    public void forgotPassword(ForgotPassword forgotPassword) {
        var email = forgotPassword.getEmail();
        var memberEntity = memberRepository.findByEmail(email)
            .orElseThrow(MemberNotFoundException::new);
        if (!memberEntity.isActive()) {
            throw new ApplicationException("email-not-verified");
        }

        var token = UUID.randomUUID().toString();
        var passwordResetEntity = PasswordResetEntity.builder()
            .token(token)
            .expiryDate(LocalDateTime.now().plusHours(4))
            .member(memberEntity)
            .build();

        deletePreviousResetPassword(memberEntity);
        try {
            passwordResetRepository.save(passwordResetEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new ApplicationException("password-reset-link-exists");
        }

        var locale = ApplicationLocaleHolder.getLocale();
        var content = messageSource.getMessage("reset-password-email", new String[]{baseUrl, token}, locale);
        var subject = messageSource.getMessage("reset-password-email-subject", null, locale);
        emailService.saveEmailRequest(email, subject, content);
    }

    public void resetPassword(ResetPassword resetPassword) {
        var passwordResetEntity = validateToken(resetPassword.getToken());
        changePassword(passwordResetEntity.getMember(), resetPassword.getPassword1());
        passwordResetRepository.delete(passwordResetEntity);
    }

    /**
     * Deletes the previous passwordResetEntity if it has expired
     */
    private void deletePreviousResetPassword(MemberEntity memberEntity) {
        passwordResetRepository.findByMember(memberEntity)
            .ifPresent(passwordResetEntity -> {
                if (LocalDateTime.now().isAfter(passwordResetEntity.getExpiryDate())) {
                    passwordResetRepository.delete(passwordResetEntity);
                    log.info("deleted previous passwordResetEntity {}", passwordResetEntity);
                }
            });
    }

    private PasswordResetEntity validateToken(String token) {
        var passwordResetEntity = passwordResetRepository.findByToken(token)
            .orElseThrow(() -> new ApplicationException("invalid-token"));
        if (LocalDateTime.now().isAfter(passwordResetEntity.getExpiryDate())) {
            passwordResetRepository.delete(passwordResetEntity);
            throw new ApplicationException("token-expired");
        }
        return passwordResetEntity;
    }

    private void changePassword(MemberEntity memberEntity, String password) {
        memberEntity.setPassword(passwordEncoder.encode(password));

        memberRepository.save(memberEntity);
        log.info("password changed for member {}", memberEntity);
    }

    private void sendVerifyEmail(MemberEntity memberEntity) {
        var token = UUID.randomUUID().toString();
        var verifyEmailEntity = VerifyEmailEntity.builder()
            .token(token)
            .member(memberEntity)
            .build();
        verifyEmailRepository.save(verifyEmailEntity);

        var locale = ApplicationLocaleHolder.getLocale();
        var content = messageSource.getMessage("verify-email-email", new String[]{baseUrl, token}, locale);
        var subject = messageSource.getMessage("verify-email-email-subject", null, locale);
        emailService.saveEmailRequest(memberEntity.getEmail(), subject, content);
    }
}
