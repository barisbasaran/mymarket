package com.mymarket.membership.login;

import com.mymarket.membership.member.MemberService;
import com.mymarket.web.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MemberService memberService;

    @PostMapping
    public LoginResponse login(@Valid @RequestBody Login login) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());
        var authentication = authenticationManager.authenticate(authenticationToken);
        memberService.checkMemberActive(login.getEmail());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails.getUsername());

        return LoginResponse.builder().token(token).build();
    }
}
