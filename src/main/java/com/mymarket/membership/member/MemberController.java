package com.mymarket.membership.member;

import com.mymarket.membership.login.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/members/self")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordService passwordService;

    @GetMapping
    public Member getMember(@AuthenticationPrincipal UserDetails userDetails) {
        return memberService.getCurrentMember()
            .orElseThrow(MemberNotLoggedInException::new);
    }

    @PutMapping
    public Member updateMember(@Valid @RequestBody Member member) {
        var currentMember = memberService.getCurrentMember()
            .orElseThrow(MemberNotLoggedInException::new);

        member.setId(currentMember.getId());
        return memberService.updateMember(member);
    }

    @PostMapping("register")
    @SneakyThrows
    public ResponseEntity<Member> register(@Valid @RequestBody Register register) {
        var memberCreated = passwordService.register(register);
        return ResponseEntity.created(new URI("/")).body(memberCreated);
    }

    @GetMapping("verify-email")
    public RedirectView verifyEmail(@RequestParam String token) {
        passwordService.verifyEmail(token);
        return new RedirectView("/membership/verify-email.html");
    }
}
