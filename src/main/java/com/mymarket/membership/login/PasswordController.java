package com.mymarket.membership.login;

import com.mymarket.membership.member.MemberNotLoggedInException;
import com.mymarket.membership.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/members/self/password")
@RequiredArgsConstructor
public class PasswordController {

    private final MemberService memberService;
    private final PasswordService passwordService;

    @PostMapping
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassword changePassword) {
        var currentMember = memberService.getCurrentMember()
            .orElseThrow(MemberNotLoggedInException::new);

        passwordService.changePassword(currentMember.getId(), changePassword);
        return ResponseEntity.ok(Map.of());
    }

    @PostMapping("reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPassword resetPassword) {
        passwordService.resetPassword(resetPassword);
        return ResponseEntity.ok(Map.of());
    }

    @PostMapping("forgot")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPassword forgotPassword) {
        passwordService.forgotPassword(forgotPassword);
        return ResponseEntity.ok(Map.of());
    }
}
